/*
 * This file is part of bisq.
 *
 * bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package io.bisq.gui.main.dao.compensation;

import de.jensd.fx.fontawesome.AwesomeDude;
import de.jensd.fx.fontawesome.AwesomeIcon;
import io.bisq.common.locale.Res;
import io.bisq.gui.Navigation;
import io.bisq.gui.common.view.*;
import io.bisq.gui.main.MainView;
import io.bisq.gui.main.dao.DaoView;
import io.bisq.gui.main.dao.compensation.active.ActiveCompensationRequestView;
import io.bisq.gui.main.dao.compensation.create.CreateCompensationRequestView;
import io.bisq.gui.main.dao.compensation.past.PastCompensationRequestView;
import io.bisq.gui.util.Colors;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import javax.inject.Inject;

@FxmlView
public class CompensationView extends ActivatableViewAndModel {

    private final ViewLoader viewLoader;
    private final Navigation navigation;

    private MenuItem create, active, past;
    private Navigation.Listener listener;

    @FXML
    private VBox leftVBox;
    @FXML
    private AnchorPane content;

    private Class<? extends View> selectedViewClass;

    @Inject
    private CompensationView(CachingViewLoader viewLoader, Navigation navigation) {
        this.viewLoader = viewLoader;
        this.navigation = navigation;
    }

    @Override
    public void initialize() {
        listener = viewPath -> {
            if (viewPath.size() != 4 || viewPath.indexOf(CompensationView.class) != 2)
                return;

            selectedViewClass = viewPath.tip();
            loadView(selectedViewClass);
        };

        ToggleGroup toggleGroup = new ToggleGroup();
        create = new MenuItem(navigation, toggleGroup, Res.get("dao.compensation.menuItem.createRequest"), CreateCompensationRequestView.class, AwesomeIcon.EDIT);
        active = new MenuItem(navigation, toggleGroup, Res.get("dao.compensation.menuItem.activeRequests"), ActiveCompensationRequestView.class, AwesomeIcon.ARROW_RIGHT);
        past = new MenuItem(navigation, toggleGroup, Res.get("dao.compensation.menuItem.pastRequests"), PastCompensationRequestView.class, AwesomeIcon.LIST);
        leftVBox.getChildren().addAll(create, active, past);
    }

    @Override
    protected void activate() {
        create.activate();
        active.activate();
        past.activate();

        navigation.addListener(listener);
        ViewPath viewPath = navigation.getCurrentPath();
        if (viewPath.size() == 3 && viewPath.indexOf(CompensationView.class) == 2 ||
                viewPath.size() == 2 && viewPath.indexOf(DaoView.class) == 1) {
            if (selectedViewClass == null)
                selectedViewClass = CreateCompensationRequestView.class;

            loadView(selectedViewClass);

        } else if (viewPath.size() == 4 && viewPath.indexOf(CompensationView.class) == 2) {
            selectedViewClass = viewPath.get(3);
            loadView(selectedViewClass);
        }
    }

    @Override
    protected void deactivate() {
        navigation.removeListener(listener);

        create.deactivate();
        active.deactivate();
        past.deactivate();
    }

    private void loadView(Class<? extends View> viewClass) {
        View view = viewLoader.load(viewClass);
        content.getChildren().setAll(view.getRoot());

        if (view instanceof CreateCompensationRequestView) create.setSelected(true);
        else if (view instanceof ActiveCompensationRequestView) active.setSelected(true);
        else if (view instanceof PastCompensationRequestView) past.setSelected(true);
    }

    public Class<? extends View> getSelectedViewClass() {
        return selectedViewClass;
    }
}


class MenuItem extends ToggleButton {

    private final ChangeListener<Boolean> selectedPropertyChangeListener;
    private final ChangeListener<Boolean> disablePropertyChangeListener;
    private final Navigation navigation;
    private final Class<? extends View> viewClass;

    MenuItem(Navigation navigation, ToggleGroup toggleGroup, String title, Class<? extends View> viewClass, AwesomeIcon awesomeIcon) {
        this.navigation = navigation;
        this.viewClass = viewClass;

        setToggleGroup(toggleGroup);
        setText(title);
        setId("account-settings-item-background-active");
        setPrefHeight(40);
        setPrefWidth(240);
        setAlignment(Pos.CENTER_LEFT);

        Label icon = new Label();
        AwesomeDude.setIcon(icon, awesomeIcon);
        icon.setTextFill(Paint.valueOf("#333"));
        icon.setPadding(new Insets(0, 5, 0, 0));
        icon.setAlignment(Pos.CENTER);
        icon.setMinWidth(25);
        icon.setMaxWidth(25);
        setGraphic(icon);

        selectedPropertyChangeListener = (ov, oldValue, newValue) -> {
            if (newValue) {
                setId("account-settings-item-background-selected");
                icon.setTextFill(Colors.BLUE);
            } else {
                setId("account-settings-item-background-active");
                icon.setTextFill(Paint.valueOf("#333"));
            }
        };

        disablePropertyChangeListener = (ov, oldValue, newValue) -> {
            if (newValue) {
                setId("account-settings-item-background-disabled");
                icon.setTextFill(Paint.valueOf("#ccc"));
            } else {
                setId("account-settings-item-background-active");
                icon.setTextFill(Paint.valueOf("#333"));
            }
        };
    }

    public void activate() {
        setOnAction((event) -> navigation.navigateTo(MainView.class, DaoView.class, CompensationView.class, viewClass));
        selectedProperty().addListener(selectedPropertyChangeListener);
        disableProperty().addListener(disablePropertyChangeListener);
    }

    public void deactivate() {
        setOnAction(null);
        selectedProperty().removeListener(selectedPropertyChangeListener);
        disableProperty().removeListener(disablePropertyChangeListener);
    }
}

