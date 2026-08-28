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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HtmlContainer;

/**
 * Base class for the two kinds of cell a {@link TableRow} can hold: a
 * {@link TableDataCell} (<code>&lt;td&gt;</code>) and a {@link TableHeaderCell}
 * (<code>&lt;th&gt;</code>). It exists so that operations that do not care
 * which kind a cell is — {@link TableRow#getCells()} and
 * {@link TableRow#removeCell(TableCell)} — have a type to speak in.
 * <p>
 * Cells hold flow content, so this class does extend {@link HtmlContainer}:
 * anything may go inside a cell.
 *
 * @since 25.3
 */
public abstract class TableCell extends HtmlContainer {

    private static final String ATTRIBUTE_HEADERS = "headers";
    private static final String ATTRIBUTE_COLSPAN = "colspan";
    private static final String ATTRIBUTE_ROWSPAN = "rowspan";

    /**
     * Creates a new empty cell.
     */
    protected TableCell() {
        super();
    }

    /**
     * Creates a new cell with the given children.
     *
     * @param components
     *            the children components.
     */
    protected TableCell(Component... components) {
        super(components);
    }

    /**
     * List equivalent of {@link #TableCell(Component...)}.
     *
     * @param components
     *            the children components.
     */
    protected TableCell(List<? extends Component> components) {
        super(components.toArray(Component[]::new));
    }

    /**
     * Sets the {@code colspan} attribute — how many columns this cell spans.
     * The default is {@code 1}.
     *
     * @param colspan
     *            a non-negative integer.
     */
    public void setColspan(int colspan) {
        setSpan(ATTRIBUTE_COLSPAN, colspan);
    }

    /**
     * Returns the value of the {@code colspan} attribute.
     *
     * @return the current colspan. Default is 1.
     */
    public int getColspan() {
        return getSpan(ATTRIBUTE_COLSPAN);
    }

    /**
     * Resets the colspan to its default value of 1.
     */
    public void resetColspan() {
        getElement().removeAttribute(ATTRIBUTE_COLSPAN);
    }

    /**
     * Sets the {@code rowspan} attribute — how many rows this cell spans. The
     * default is {@code 1}.
     *
     * @param rowspan
     *            a non-negative integer.
     */
    public void setRowspan(int rowspan) {
        setSpan(ATTRIBUTE_ROWSPAN, rowspan);
    }

    /**
     * Returns the value of the {@code rowspan} attribute.
     *
     * @return the current rowspan. Default is 1.
     */
    public int getRowspan() {
        return getSpan(ATTRIBUTE_ROWSPAN);
    }

    /**
     * Resets the rowspan to its default value of 1.
     */
    public void resetRowspan() {
        getElement().removeAttribute(ATTRIBUTE_ROWSPAN);
    }

    private void setSpan(String attribute, int span) {
        if (span < 0) {
            throw new IllegalArgumentException(
                    attribute + " must be a non-negative integer value");
        }
        getElement().setAttribute(attribute, String.valueOf(span));
    }

    private int getSpan(String attribute) {
        String span = getElement().getAttribute(attribute);
        return span == null ? 1 : Integer.parseInt(span);
    }

    /**
     * Sets the {@code headers} attribute — a list of ids referring to the
     * <code>&lt;th&gt;</code> cells that label this cell. Assistive
     * technologies use it to read out the right headers when navigating complex
     * tables, where {@link TableHeaderCell#setScope(TableHeaderCell.Scope)
     * scope} alone isn't enough to disambiguate.
     * <p>
     * An empty array removes the attribute; call {@link #resetHeaders()} to do
     * the same without an argument, as an empty call would be ambiguous between
     * this overload and {@link #setHeaders(TableHeaderCell...)}.
     *
     * @param ids
     *            the ids of the header cells, in any order.
     */
    public void setHeaders(String... ids) {
        setHeaders(ids == null ? List.of() : Arrays.asList(ids));
    }

    /**
     * List equivalent of {@link #setHeaders(String...)}. An empty list (or
     * {@code null}) clears the attribute.
     *
     * @param ids
     *            the ids of the header cells, in any order.
     */
    public void setHeaders(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            getElement().removeAttribute(ATTRIBUTE_HEADERS);
            return;
        }
        for (String id : ids) {
            Objects.requireNonNull(id, "header id must not be null");
        }
        getElement().setAttribute(ATTRIBUTE_HEADERS, String.join(" ", ids));
    }

    /**
     * Convenience overload that takes header cells directly and uses their
     * {@code id} attributes. Each cell must have an id set.
     *
     * @param headerCells
     *            the header cells whose ids should be referenced.
     * @throws IllegalArgumentException
     *             if any of the given cells does not have an id set.
     */
    public void setHeaders(TableHeaderCell... headerCells) {
        setHeadersByCells(
                headerCells == null ? List.of() : Arrays.asList(headerCells));
    }

    /**
     * List equivalent of {@link #setHeaders(TableHeaderCell...)}.
     *
     * @param headerCells
     *            the header cells whose ids should be referenced.
     * @throws IllegalArgumentException
     *             if any of the given cells does not have an id set.
     */
    public void setHeadersByCells(List<? extends TableHeaderCell> headerCells) {
        if (headerCells == null || headerCells.isEmpty()) {
            getElement().removeAttribute(ATTRIBUTE_HEADERS);
            return;
        }
        List<String> ids = new ArrayList<>(headerCells.size());
        for (TableHeaderCell cell : headerCells) {
            ids.add(cell.getId().orElseThrow(() -> new IllegalArgumentException(
                    "Header cell must have an id to be referenced via the headers attribute")));
        }
        setHeaders(ids);
    }

    /**
     * Returns the ids of the header cells associated with this cell via the
     * {@code headers} attribute, in the order they appear, or an empty list if
     * the attribute is not set.
     *
     * @return the header ids, never {@code null}.
     */
    public List<String> getHeaders() {
        String value = getElement().getAttribute(ATTRIBUTE_HEADERS);
        if (value == null || value.isEmpty()) {
            return List.of();
        }
        return List.of(value.split("\\s+"));
    }

    /**
     * Removes the {@code headers} attribute from this cell.
     */
    public void resetHeaders() {
        getElement().removeAttribute(ATTRIBUTE_HEADERS);
    }
}
