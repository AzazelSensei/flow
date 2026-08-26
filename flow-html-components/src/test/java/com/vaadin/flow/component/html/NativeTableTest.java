/*
 * Copyright 2000-2026 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.flow.component.html;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NativeTableTest extends ComponentTest {
    // Actual test methods in super class

    @BeforeEach
    @Override
    void setup() throws IntrospectionException, InstantiationException,
            IllegalAccessException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException {
        whitelistProperty("captionText");
        super.setup();
    }

    @Test
    void getCaption() {
        var component = (NativeTable) getComponent();
        NativeTableCaption caption = component.getCaption();
        AssertUtils.assertEquals(component.getChildren().toList().get(0),
                caption, "Caption does not match");
    }

    @Test
    void addsCaptionAsFirstChild() {
        var component = (NativeTable) getComponent();
        assertEquals(0, component.getChildren().count());
        component.getHead();
        component.addBody();
        component.getFoot();
        var caption = component.getCaption();
        assertEquals(4, component.getChildren().count());
        AssertUtils.assertEquals(caption,
                component.getChildren().findFirst().orElseThrow(),
                "Caption is not the first child");
        AssertUtils.assertEquals(caption.getParent().orElseThrow(), component,
                "NativeTable is not the caption's father");
    }

    @Test
    void setCaptionText() {
        var component = (NativeTable) getComponent();
        String expectedText = "Test caption text.";
        component.setCaptionText(expectedText);
        var caption = component.getCaption();
        assertEquals(expectedText, caption.getText());
    }

    @Test
    void getCaptionText_emptyWhenNoCaption() {
        var component = (NativeTable) getComponent();
        assertEquals("", component.getCaptionText());
        assertTrue(child(component, NativeTableCaption.class).isEmpty());
    }

    @Test
    void getCaptionText_returnsCaptionText() {
        var component = (NativeTable) getComponent();
        String expectedText = "Test caption text.";
        var caption = component.getCaption();
        caption.setText(expectedText);
        assertEquals(expectedText, component.getCaptionText());
    }

    @Test
    void removeCaption() {
        var component = (NativeTable) getComponent();
        var caption = component.getCaption();
        component.removeCaption();
        assertTrue(caption.getParent().isEmpty());
        assertTrue(child(component, NativeTableCaption.class).isEmpty());
    }

    @Test
    void getHead() {
        var component = (NativeTable) getComponent();
        assertEquals(0, component.getChildren().count());
        NativeTableHeader head = component.getHead();
        AssertUtils.assertEquals(component, head.getParent().orElseThrow(),
                "head was not added");
    }

    @Test
    void addHeadAfterCaption() {
        var component = (NativeTable) getComponent();
        component.getCaption();
        var head = component.getHead();
        assertEquals(2, component.getChildren().count());
        int headIndex = component.getChildren().toList().indexOf(head);
        assertEquals(1, headIndex);
    }

    @Test
    void removeHead() {
        var component = (NativeTable) getComponent();
        NativeTableHeader head = component.getHead();
        component.removeHead();
        assertTrue(head.getParent().isEmpty());
        assertTrue(child(component, NativeTableHeader.class).isEmpty());
    }

    @Test
    void getFoot() {
        var component = (NativeTable) getComponent();
        assertEquals(0, component.getChildren().count());
        NativeTableFooter footer = component.getFoot();
        AssertUtils.assertEquals(component, footer.getParent().orElseThrow(),
                "footer was not added");
    }

    @Test
    void removeFoot() {
        var component = (NativeTable) getComponent();
        NativeTableFooter footer = component.getFoot();
        component.removeFoot();
        assertTrue(footer.getParent().isEmpty());
        assertTrue(child(component, NativeTableFooter.class).isEmpty());
    }

    @Test
    void addBody() {
        var component = (NativeTable) getComponent();
        component.addBody();
        assertEquals(1, component.getChildren().count());
        component.addBody();
        assertEquals(2, component.getChildren().count());
    }

    @Test
    void addBodyAfterCaption() {
        var component = (NativeTable) getComponent();
        component.getCaption();
        var body = component.addBody();
        assertEquals(1, component.getChildren().toList().indexOf(body));
    }

    @Test
    void addBodyAfterHeader() {
        var component = (NativeTable) getComponent();
        component.getHead();
        var body = component.addBody();
        assertEquals(1, component.getChildren().toList().indexOf(body));
    }

    @Test
    void addBodyAfterBothCaptionAndHeader() {
        var component = (NativeTable) getComponent();
        component.getCaption();
        component.getHead();
        var body = component.addBody();
        assertEquals(2, component.getChildren().toList().indexOf(body));
    }

    @Test
    void addBodyBeforeFoot() {
        var component = (NativeTable) getComponent();
        component.getFoot();
        var body = component.addBody();
        assertEquals(0, component.getChildren().toList().indexOf(body));
        assertEquals(1,
                component.getChildren().toList().indexOf(component.getFoot()));
    }

    @Test
    void getBody() {
        var component = (NativeTable) getComponent();
        var body = component.getBody();
        assertEquals(1, component.getChildren().count());
        component.addBody();
        assertEquals(2, component.getChildren().count());
        var secondCallBody = component.getBody();
        AssertUtils.assertEquals(body, secondCallBody,
                "No new body should've been created");
    }

    @Test
    void getBodies() {
        var component = (NativeTable) getComponent();
        for (int i = 0; i < 10; i++) {
            component.addBody();
        }
        List<NativeTableBody> bodies = component.getBodies();
        for (NativeTableBody body : bodies) {
            AssertUtils.assertEquals(component, body.getParent().orElseThrow(),
                    "Body is not a child of table");
        }
    }

    @Test
    void removeBodyByReference() {
        var component = (NativeTable) getComponent();
        var body0 = component.addBody();
        var body1 = component.addBody();
        var body2 = component.addBody();
        component.removeBody(body1);
        assertTrue(body0.getParent().isPresent());
        assertTrue(body1.getParent().isEmpty());
        assertTrue(body2.getParent().isPresent());
    }

    @Test
    void addRow_autoCreatesBody() {
        var table = (NativeTable) getComponent();
        NativeTableRow row = table.addRow();
        assertTrue(child(table, NativeTableHeader.class).isEmpty());
        assertEquals(1, table.getBodies().size());
        assertEquals(1, table.getBody().getRows().size());
        AssertUtils.assertEquals(table.getBody(), row.getParent().orElseThrow(),
                "row must live inside the auto-created tbody");
    }

    @Test
    void addRow_withCellTexts_createsDataCells() {
        var table = (NativeTable) getComponent();
        NativeTableRow row = table.addRow("Alice", "30", "Blue");
        assertEquals(3, row.getDataCells().size());
        assertEquals("Alice", row.getDataCells().get(0).getText());
        assertEquals("30", row.getDataCells().get(1).getText());
        assertEquals("Blue", row.getDataCells().get(2).getText());
        assertEquals(0, row.getHeaderCells().size());
    }

    @Test
    void addHeaderRow_autoCreatesThead() {
        var table = (NativeTable) getComponent();
        NativeTableRow row = table.addHeaderRow("Name", "Age");
        assertTrue(child(table, NativeTableHeader.class).isPresent());
        assertEquals(1, table.getHead().getRows().size());
        assertEquals(2, row.getHeaderCells().size());
        assertEquals("Name", row.getHeaderCells().get(0).getText());
    }

    @Test
    void addFooterRow_autoCreatesTfoot() {
        var table = (NativeTable) getComponent();
        NativeTableRow row = table.addFooterRow("Total", "55");
        assertTrue(child(table, NativeTableFooter.class).isPresent());
        assertEquals(1, table.getFoot().getRows().size());
        assertEquals(2, row.getDataCells().size());
    }

    @Test
    void mdnTutorialStyleConstruction() {
        // Mirrors the MDN "HTML table basics" walkthrough: caption, header
        // row, body rows. Verifies the resulting structure is spec-compliant
        // (caption first, thead before tbody) and that all rows landed in
        // the right wrappers.
        var table = (NativeTable) getComponent();
        table.setCaptionText("People");
        table.addHeaderRow("Name", "Age", "Color");
        table.addRow("Alice", "30", "Blue");
        table.addRow("Bob", "25", "Green");

        assertEquals("People", table.getCaptionText());
        assertEquals(1, table.getHead().getRows().size());
        assertEquals(2, table.getBody().getRows().size());

        var children = table.getChildren().toList();
        assertEquals(table.getCaption(), children.get(0));
        assertEquals(table.getHead(), children.get(1));
        assertEquals(table.getBody(), children.get(2));
    }

    @Test
    void addRows_addsExistingRowsToBody() {
        var table = (NativeTable) getComponent();
        var r1 = new NativeTableRow();
        var r2 = new NativeTableRow();
        table.addRows(r1, r2);
        assertEquals(2, table.getBody().getRows().size());
        AssertUtils.assertEquals(table.getBody(), r1.getParent().orElseThrow(),
                "r1 must be a child of tbody");
        AssertUtils.assertEquals(table.getBody(), r2.getParent().orElseThrow(),
                "r2 must be a child of tbody");
    }

    @Test
    void addCaption_createsAndAppendsComponents() {
        var table = (NativeTable) getComponent();
        var span = new Span("Cars");
        var caption = table.addCaption(span);
        assertEquals(1, caption.getComponentCount());
        assertEquals(span, caption.getComponentAt(0));
        assertEquals(table.getCaption(), caption);
    }

    @Test
    void addColumnGroup_insertedAfterCaptionBeforeHead() {
        var table = (NativeTable) getComponent();
        table.getCaption();
        table.getHead();
        var group = table.addColumnGroup();
        var children = table.getChildren().toList();
        assertEquals(table.getCaption(), children.get(0));
        assertEquals(group, children.get(1));
        assertEquals(table.getHead(), children.get(2));
    }

    @Test
    void addColumnGroup_beforeHeadEvenIfHeadAddedLater() {
        var table = (NativeTable) getComponent();
        var group = table.addColumnGroup();
        var head = table.getHead();
        var children = table.getChildren().toList();
        assertEquals(group, children.get(0));
        assertEquals(head, children.get(1));
    }

    @Test
    void addColumnGroup_withColumns() {
        var table = (NativeTable) getComponent();
        var c1 = new NativeTableColumn();
        var c2 = new NativeTableColumn(2);
        var group = table.addColumnGroup(c1, c2);
        assertEquals(2, group.getColumns().size());
        assertEquals(List.of(group), table.getColumnGroups());
    }

    @Test
    void multipleColumnGroups_appearInInsertionOrder() {
        var table = (NativeTable) getComponent();
        var g1 = table.addColumnGroup();
        var g2 = table.addColumnGroup();
        var children = table.getChildren().toList();
        assertEquals(g1, children.get(0));
        assertEquals(g2, children.get(1));
    }

    @Test
    void removeColumnGroup() {
        var table = (NativeTable) getComponent();
        var g1 = table.addColumnGroup();
        var g2 = table.addColumnGroup();
        table.removeColumnGroup(g1);
        assertEquals(List.of(g2), table.getColumnGroups());
        assertTrue(g1.getParent().isEmpty());
    }

    @Test
    void bodyAppendIndex_accountsForColumnGroups() {
        // caption + 2 colgroups + thead → tbody must land at index 4
        var table = (NativeTable) getComponent();
        table.setCaptionText("x");
        table.addColumnGroup();
        table.addColumnGroup();
        table.getHead();
        var body = table.addBody();
        assertEquals(4, table.getChildren().toList().indexOf(body));
    }

    @Test
    void getRows_emptyWhenNoSections() {
        var table = (NativeTable) getComponent();
        assertTrue(table.getRows().isEmpty());
    }

    @Test
    void getRows_returnsHeadBodiesFootInOrder() {
        var table = (NativeTable) getComponent();
        var headRow = table.addHeaderRow("Name");
        var bodyRow1 = table.addRow("Alice");
        var bodyRow2 = table.addRow("Bob");
        var footRow = table.addFooterRow("Total");

        var rows = table.getRows();
        assertEquals(List.of(headRow, bodyRow1, bodyRow2, footRow), rows);
    }

    @Test
    void getRows_concatenatesMultipleBodies() {
        var table = (NativeTable) getComponent();
        var b1Row = table.getBody().addRow();
        var b2Row = table.addBody().addRow();
        var rows = table.getRows();
        assertEquals(List.of(b1Row, b2Row), rows);
    }

    @Test
    void getRows_isUnmodifiable() {
        var table = (NativeTable) getComponent();
        table.addRow();
        var rows = table.getRows();
        assertThrows(UnsupportedOperationException.class,
                () -> rows.add(new NativeTableRow()));
    }

    @Test
    void removeAllRows_clearsRowsButKeepsSections() {
        var table = (NativeTable) getComponent();
        table.addHeaderRow("h");
        table.addRow("b1");
        table.addRow("b2");
        table.addFooterRow("f");

        table.removeAllRows();

        assertTrue(table.getRows().isEmpty());
        // Sections themselves remain
        assertTrue(child(table, NativeTableHeader.class).isPresent());
        assertEquals(1, table.getBodies().size());
        assertTrue(child(table, NativeTableFooter.class).isPresent());
        assertEquals(0, table.getHead().getRows().size());
        assertEquals(0, table.getBody().getRows().size());
        assertEquals(0, table.getFoot().getRows().size());
    }

    @Test
    void removeAllRows_isNoOpOnEmptyTable() {
        var table = (NativeTable) getComponent();
        table.removeAllRows();
        assertTrue(table.getRows().isEmpty());
    }

    @Test
    void sectionsAddedAsPlainChildren_areFoundByAccessors() {
        var table = (NativeTable) getComponent();
        NativeTableHeader head = new NativeTableHeader();
        NativeTableBody body = new NativeTableBody();
        table.add(head, body);

        assertEquals(head, child(table, NativeTableHeader.class).orElse(null));
        assertEquals(List.of(body), table.getBodies());
        assertEquals(body, table.getBody());
    }

    @Test
    void addBody_afterGenericallyAddedFoot_isInsertedBeforeIt() {
        var table = (NativeTable) getComponent();
        NativeTableFooter foot = new NativeTableFooter();
        table.add(foot);

        NativeTableBody body = table.addBody();

        assertEquals(0, table.indexOf(body));
        assertEquals(1, table.indexOf(foot));
    }

    private static <T extends Component> Optional<T> child(NativeTable table,
            Class<T> type) {
        return ComponentUtil.getFirstChildOfType(table, type);
    }

}
