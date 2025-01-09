/*
 * This file is generated by jOOQ.
 */
package com.tdedsh.generated.enums;


import org.jooq.Catalog;
import org.jooq.EnumType;
import org.jooq.Schema;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public enum TasksStatus implements EnumType {

    TODO("TODO"),

    IN_PROGRESS("IN_PROGRESS"),

    DONE("DONE");

    private final String literal;

    private TasksStatus(String literal) {
        this.literal = literal;
    }

    @Override
    public Catalog getCatalog() {
        return null;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getLiteral() {
        return literal;
    }

    /**
     * Lookup a value of this EnumType by its literal
     */
    public static TasksStatus lookupLiteral(String literal) {
        return EnumType.lookupLiteral(TasksStatus.class, literal);
    }
}
