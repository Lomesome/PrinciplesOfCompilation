package pers.lomesome.compliation.view.mywidgets;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pers.lomesome.compliation.model.PropertyWord;

public class AnalyzerTableView<T> extends TableView<T> {

    public AnalyzerTableView(String[] cols, ObservableList<T> list, int width){
        this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        for(String col : cols){
            TableColumn theCol = new TableColumn<>(col);
            theCol.setSortable(false);
            theCol.setResizable(false);
            // 设置宽度
            theCol.setPrefWidth(width);
            // 下面的属性名
            theCol.setCellValueFactory(new PropertyValueFactory<>(col));
            this.getColumns().add(theCol);
        }
        this.setItems(list);
        this.setMaxWidth(300);
        this.getColumns().addListener(new ListChangeListener() {
            boolean isturnback = false;

            @Override
            public void onChanged(Change c) {

                if (!isturnback) {
                    while (c.next()) {
                        if (!c.wasPermutated() && !c.wasUpdated()) {
                            isturnback = true;
                            getColumns().setAll(c.getRemoved());
                        }
                    }
                }
                else {
                    isturnback = false;
                }
            }
        });
    }
}