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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NativeTableRowTest extends ComponentTest {
    // Most tests in super class

    @Override
    protected void addProperties() {
        // Component defines no new properties
    }

    @Test
    void constructor_acceptsCells() {
        NativeTableCell td = new NativeTableCell("data");
        NativeTableHeaderCell th = new NativeTableHeaderCell("hdr");
        NativeTableRow row = new NativeTableRow(th, td);
        assertEquals(2, row.getCells().size());
        assertEquals(1, row.getDataCells().size());
        assertEquals(1, row.getHeaderCells().size());
    }

    @Test
    void addDataCell_appendsTd() {
        NativeTableRow row = new NativeTableRow();
        NativeTableCell td = row.addDataCell();
        assertEquals(1, row.getDataCells().size());
        assertEquals(td, row.getDataCells().get(0));
    }

    @Test
    void addHeaderCell_appendsTh() {
        NativeTableRow row = new NativeTableRow();
        NativeTableHeaderCell th = row.addHeaderCell("Name");
        assertEquals(1, row.getHeaderCells().size());
        assertEquals(th, row.getHeaderCells().get(0));
        assertEquals("Name", th.getText());
    }

    @Test
    void addDataCells_appendsAll() {
        NativeTableRow row = new NativeTableRow();
        NativeTableRow result = row.addDataCells("a", "b", "c");
        assertEquals(row, result);
        assertEquals(3, row.getDataCells().size());
        assertEquals("a", row.getDataCells().get(0).getText());
        assertEquals("b", row.getDataCells().get(1).getText());
        assertEquals("c", row.getDataCells().get(2).getText());
    }

    @Test
    void addHeaderCells_appendsAll() {
        NativeTableRow row = new NativeTableRow();
        NativeTableRow result = row.addHeaderCells("Name", "Age");
        assertEquals(row, result);
        assertEquals(2, row.getHeaderCells().size());
    }

    @Test
    void addCells_appendsPreBuiltCells() {
        NativeTableRow row = new NativeTableRow();
        NativeTableHeaderCell th = new NativeTableHeaderCell("Name");
        NativeTableCell td = new NativeTableCell("Alice");
        row.addCells(th, td);
        assertEquals(1, row.getHeaderCells().size());
        assertEquals(1, row.getDataCells().size());
    }

    @Test
    void constructor_wrapsNonCellComponentsInDataCell() {
        Span span = new Span("hi");
        NativeTableHeaderCell th = new NativeTableHeaderCell("Name");
        NativeTableRow row = new NativeTableRow(span, th);

        assertEquals(2, row.getCells().size());
        // span got wrapped in a new NativeTableCell
        NativeTableCell wrapper = row.getDataCells().get(0);
        assertEquals(span, wrapper.getChildren().findFirst().orElseThrow());
        // header cell preserved as-is
        assertEquals(th, row.getHeaderCells().get(0));
    }

    @Test
    void addCells_wrapsNonCellComponentsInDataCell() {
        NativeTableRow row = new NativeTableRow();
        Span span = new Span("hi");
        row.addCells(span);

        assertEquals(1, row.getDataCells().size());
        assertEquals(span, row.getDataCells().get(0).getChildren().findFirst()
                .orElseThrow());
    }

    @Test
    void addCells_listOverloadMatchesVarargs() {
        NativeTableRow row = new NativeTableRow();
        NativeTableCell td = new NativeTableCell("a");
        NativeTableHeaderCell th = new NativeTableHeaderCell("h");
        row.addCells(java.util.List.of(td, th));

        assertEquals(2, row.getCells().size());
        assertEquals(td, row.getCells().get(0));
        assertEquals(th, row.getCells().get(1));
    }

    @Test
    void addDataCells_listOverloadMatchesVarargs() {
        NativeTableRow row = new NativeTableRow();
        row.addDataCells(java.util.List.of("a", "b", "c"));
        assertEquals(3, row.getDataCells().size());
        assertEquals("a", row.getDataCells().get(0).getText());
        assertEquals("c", row.getDataCells().get(2).getText());
    }

    @Test
    void removeCell_dropsFromRow() {
        NativeTableRow row = new NativeTableRow();
        NativeTableHeaderCell th = row.addHeaderCell("Name");
        NativeTableCell td = row.addDataCell("Alice");
        row.removeCell(th);
        assertEquals(1, row.getCells().size());
        assertEquals(td, row.getCells().get(0));
    }

    @Test
    void addRowHeaderCell_setsScopeRow() {
        NativeTableRow row = new NativeTableRow();
        NativeTableHeaderCell th = row.addRowHeaderCell("Cucumber");
        assertEquals("Cucumber", th.getText());
        assertEquals(NativeTableHeaderCell.Scope.ROW,
                th.getScope().orElseThrow());
        assertEquals(1, row.getHeaderCells().size());
        assertEquals(th, row.getHeaderCells().get(0));
    }
}
