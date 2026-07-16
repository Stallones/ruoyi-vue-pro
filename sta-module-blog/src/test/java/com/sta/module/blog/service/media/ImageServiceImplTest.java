package com.sta.module.blog.service.media;

import cn.iocoder.yudao.framework.test.core.ut.BaseMockitoUnitTest;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import com.sta.module.blog.constants.BlogInfraConstants;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.dal.mysql.media.ImageMapper;
import com.sta.module.blog.enums.TypeEnum;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static com.sta.module.blog.enums.ErrorCodeConstants.IMAGE_FILE_SIZE_EXCEEDED;
import static com.sta.module.blog.enums.ErrorCodeConstants.IMAGE_FILE_TYPE_INVALID;
import static com.sta.module.blog.enums.ErrorCodeConstants.IMAGE_TYPE_INVALID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ImageServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private ImageServiceImpl imageService;

    @Mock
    private ImageMapper imageMapper;

    @Mock
    private FileApi fileApi;

    @Test
    public void uploadImage_avatar_success() throws IOException {
        // 准备参数
        MultipartFile file = new MockMultipartFile("avatar.jpg", "avatar.jpg", "image/jpeg", "content".getBytes(StandardCharsets.UTF_8));
        Long loginUserId = 100L;
        String expectedUrl = "http://127.0.0.1:9000/yudao/blog/avatar/20260716/avatar.jpg";
        when(fileApi.createFile(eq(BlogInfraConstants.BLOG_FILE_CONFIG_ID), any(byte[].class),
                eq("avatar.jpg"), eq("blog/avatar"), eq("image/jpeg"))).thenReturn(expectedUrl);

        // 调用
        ImageDO result = imageService.uploadImage(file, TypeEnum.IMG_AVATAR, null, loginUserId);

        // 断言返回值
        assertNotNull(result);
        assertEquals(TypeEnum.IMG_AVATAR, result.getType());
        assertEquals(loginUserId, result.getDataId());
        assertEquals(expectedUrl, result.getPath());
        assertEquals(file.getSize(), result.getSize());
        assertEquals("jpg", result.getExtension());
        // 断言 blog_image 被插入
        ArgumentCaptor<ImageDO> imageCaptor = ArgumentCaptor.forClass(ImageDO.class);
        verify(imageMapper).insert(imageCaptor.capture());
        ImageDO inserted = imageCaptor.getValue();
        assertEquals(TypeEnum.IMG_AVATAR, inserted.getType());
        assertEquals(loginUserId, inserted.getDataId());
        assertEquals(expectedUrl, inserted.getPath());
    }

    @Test
    public void uploadImage_articleCover_mapsToBlogArticleDirectory() throws IOException {
        // 准备参数
        MultipartFile file = new MockMultipartFile("cover.jpg", "cover.jpg", "image/jpeg", "content".getBytes(StandardCharsets.UTF_8));
        Long articleId = 200L;
        String expectedUrl = "http://127.0.0.1:9000/yudao/blog/article/20260716/cover.jpg";
        when(fileApi.createFile(eq(BlogInfraConstants.BLOG_FILE_CONFIG_ID), any(byte[].class),
                eq("cover.jpg"), eq("blog/article"), eq("image/jpeg"))).thenReturn(expectedUrl);

        // 调用
        ImageDO result = imageService.uploadImage(file, TypeEnum.IMG_COVER, articleId, 100L);

        // 断言
        assertEquals(articleId, result.getDataId());
        assertEquals("blog/article", extractDirectoryFromUploadCall());
    }

    @Test
    public void uploadImage_invalidFileType_throws() {
        // 准备参数
        MultipartFile file = new MockMultipartFile("avatar.txt", "avatar.txt", "text/plain", "content".getBytes(StandardCharsets.UTF_8));

        // 调用并断言异常
        assertServiceException(() -> imageService.uploadImage(file, TypeEnum.IMG_AVATAR, null, 100L),
                IMAGE_FILE_TYPE_INVALID);
    }

    @Test
    public void uploadImage_oversized_throws() {
        // 准备参数：3MB 的 png
        byte[] content = new byte[3 * 1024 * 1024];
        MultipartFile file = new MockMultipartFile("avatar.png", "avatar.png", "image/png", content);

        // 调用并断言异常
        assertServiceException(() -> imageService.uploadImage(file, TypeEnum.IMG_AVATAR, null, 100L),
                IMAGE_FILE_SIZE_EXCEEDED);
    }

    @Test
    public void uploadImage_unknownType_throws() {
        // 准备参数
        MultipartFile file = new MockMultipartFile("avatar.jpg", "avatar.jpg", "image/jpeg", "content".getBytes(StandardCharsets.UTF_8));

        // 调用并断言异常
        assertServiceException(() -> imageService.uploadImage(file, TypeEnum.ART, null, 100L),
                IMAGE_TYPE_INVALID);
    }

    private String extractDirectoryFromUploadCall() {
        ArgumentCaptor<String> directoryCaptor = ArgumentCaptor.forClass(String.class);
        verify(fileApi).createFile(eq(BlogInfraConstants.BLOG_FILE_CONFIG_ID), any(byte[].class),
                any(), directoryCaptor.capture(), any());
        return directoryCaptor.getValue();
    }

}
