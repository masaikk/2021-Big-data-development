package xyz.masaikk.win;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import xyz.masaikk.comp.GetProperties;

import java.io.File;
import java.io.IOException;

public class Win1 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String setUpFilePath = GetProperties.getValueByKey("config.properties", "fileFolderPath");

        Label filenameLabel = new Label("当前选择的文件夹：" + setUpFilePath);
        Button btnChoosePath = new Button("Choose path");
        /**
         * 设置btnHello按钮点击事件
         * 这里使用了Java8的Lambda表达式。setOnAction的参数为EventHandler<ActionEvent> value
         * EventHandler为一个接口，所以我们有三种方式实现EventHandler接口：
         * 1. 创建一个内部类
         * 2. 创建一个匿名类
         * 3. 使用Lambda表达式（适用于函数体不大的情况）
         */
        btnChoosePath.setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(new Stage());
            String path = file.getPath();
            System.out.println(path);
            try {
                GetProperties.writeProperties("config.properties", "fileFolderPath", path);
                filenameLabel.setText("当前选择的文件夹：" + path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        /**
         *  BorderPane是一个用于布局的Pane，BoerderPane将面板分割为上下左右中五部分。
         *  我们可以将UI控件放置在BorderPane的上下左右和中间。
         *  这里将将Button放置在中间。
         */
        BorderPane pane = new BorderPane();
        pane.setTop(filenameLabel);
        pane.setCenter(btnChoosePath);

        // 将pane加入到Scene中
        Scene scene = new Scene(pane, 400, 500);

        // 设置stage的scene，然后显示我们的stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Week1 APP");
        primaryStage.show();

    }

    public static void main(String[] args) {
        // JavaFX中main函数必须需要调用launch函数
        launch(args);
    }


}

