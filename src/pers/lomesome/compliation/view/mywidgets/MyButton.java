package pers.lomesome.compliation.view.mywidgets;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MyButton extends Button {

    public MyButton(String imagePath, String cssName) {
        this.setPrefSize(23, 23);  //设置按钮最佳宽度和高度
        this.getStyleClass().add(cssName);  //给按钮添加Css
        this.setGraphic(makeImageView(imagePath, 20, 20));  //设置按钮上的图片
    }

    public MyButton(String imagePath, String cssName, int width, int height) {
        this.setMinSize(width, height);
        this.getStyleClass().add(cssName);
        this.setGraphic(makeImageView(imagePath, width - 3, height - 3));
    }

    public ImageView makeImageView(String imagePath, int w, int h) {
        ImageView imageView = new ImageView(this.getClass().getResource(imagePath).toString());
        imageView.setFitWidth(w);
        imageView.setFitHeight(h);
        return imageView;
    }

    public void setImageView(String imagePath){
        this.setGraphic(makeImageView(imagePath, (int) (this.getWidth() - 3), (int) (this.getHeight() - 3)));
    }
}
