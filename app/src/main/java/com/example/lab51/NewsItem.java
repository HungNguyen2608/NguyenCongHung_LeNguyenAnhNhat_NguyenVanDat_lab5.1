package com.example.lab51;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsItem implements Serializable {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String title;
    public String imageUrl;
    public String url;

    public NewsItem(String imageUrl, String title, String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.url = url;
    }



    public String getTitle() {
        return title;
    }
    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }

    public static List<NewsItem> DSNews(){
        List<NewsItem> listNews = new ArrayList<NewsItem>();
        listNews.add(new NewsItem("https://file1.hutech.edu.vn/file/editor/homepage1/400808-dsc00497.jpg",
                "Thí sinh nộp lệ phí trực tuyến đại học theo 6 cụm tỉnh, thành",
                "https://www.hutech.edu.vn/tuyensinh/tin-tuc/tin-tuyen-sinh/14611940-thi-sinh-nop-le-phi-truc-tuyen-dai-hoc-theo-6-cum-tinh-thanh"));
        return listNews;
    }
}

