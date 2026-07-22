# 从 dev-jdk17 合并更新到 sta/blog-server，自动保持已删模块的删除状态
# 用法: powershell -File scripts/merge-from-dev.ps1

$DELETED_MODULES = @(
    "yudao-module-ai",
    "yudao-module-bpm",
    "yudao-module-crm",
    "yudao-module-erp",
    "yudao-module-im",
    "yudao-module-iot",
    "yudao-module-mall",
    "yudao-module-member",
    "yudao-module-mes",
    "yudao-module-mp",
    "yudao-module-pay",
    "yudao-module-report",
    "yudao-module-wms"
)

Write-Host "Merging dev-jdk17..." -ForegroundColor Cyan
git merge dev-jdk17

if ($LASTEXITCODE -ne 0) {
    Write-Host "Handling modify/delete conflicts (keep deletions)..." -ForegroundColor Yellow
    $status = git status --porcelain
    foreach ($line in $status) {
        if ($line -match "^(DU|AU|UD) ") {
            $file = $line.Substring(3).Trim()
            Write-Host "  Keeping deletion: $file" -ForegroundColor Yellow
            git rm "`"$file`"" 2>$null
        }
    }
}

# 清理被带回来的已删模块目录
Write-Host "Cleaning up deleted modules..." -ForegroundColor Cyan
foreach ($module in $DELETED_MODULES) {
    if (Test-Path $module) {
        Write-Host "  Removing: $module" -ForegroundColor Yellow
        git rm -r --ignore-unmatch $module
        Remove-Item -Recurse -Force $module -ErrorAction SilentlyContinue
    }
}

# 检查是否还有未解决的内容冲突
$remaining = git status --porcelain | Select-String "^(DD|AU|UD|UA|DU|AA|U)"
if ($remaining) {
    Write-Host "`nUnresolved content conflicts (fix manually):" -ForegroundColor Red
    git diff --name-only --diff-filter=U
    exit 1
}

git commit --no-edit
Write-Host "`nMerge complete." -ForegroundColor Green
