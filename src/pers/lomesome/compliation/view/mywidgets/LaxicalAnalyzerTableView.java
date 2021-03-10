package pers.lomesome.compliation.view.mywidgets;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pers.lomesome.compliation.model.PropertyWord;

public class LaxicalAnalyzerTableView extends TableView {

    public LaxicalAnalyzerTableView(String[] cols, ObservableList<PropertyWord> list){
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(String col : cols){
            TableColumn theCol = new TableColumn(col);
            theCol.setSortable(false);
            theCol.setResizable(false);
            // 设置宽度
            theCol.setMinWidth(150);
            // 下面的属性名
            theCol.setCellValueFactory(new PropertyValueFactory<>(col));
            this.getColumns().add(theCol);
        }
        this.setItems(list);
        this.setMaxWidth(300);
    }
}