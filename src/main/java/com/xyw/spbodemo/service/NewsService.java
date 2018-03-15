package com.xyw.spbodemo.service;

import com.xyw.spbodemo.dao.NewsDao;
import com.xyw.spbodemo.model.News;
import com.xyw.spbodemo.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class NewsService {


    @Autowired
    private NewsDao newsDao;

    public int addNews(News news){
        newsDao.addNews(news);
        return news.getId();
    }

    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDao.selectByUserIdAndOffset(userId, offset, limit);
    }

    public String saveImage(MultipartFile file) throws IOException {
        //xx.  = adfa.jpg
        int dotPos = file.getOriginalFilename().lastIndexOf(".");
        if (dotPos < 0) {
            return null;
        }
        String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
        if (!ToutiaoUtil.isFileAllowed(fileExt)) {
            return null;
        }
        // fsd-sfsdf-sfsd-sdfsd
        String fileName = UUID.randomUUID().toString().
                replaceAll("-", "") + "." + fileExt;

        Files.copy(file.getInputStream(), new File(ToutiaoUtil.
                IMAGE_DIR + fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        //xxxx.jpg
        return ToutiaoUtil.TOUTIAO_DOMAIN + "image?name=" + fileName;
    }
}
