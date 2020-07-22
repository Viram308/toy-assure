package com.assure.spring;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.io.Serializable;
import java.util.Locale;

public class PhysicalNamingStrategyImpl extends PhysicalNamingStrategyStandardImpl implements Serializable {

    public static final String TABLE_NAME_PREFIX = "assure_";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier newIdentifier = new Identifier(TABLE_NAME_PREFIX + addUnderscores(name.getText()), name.isQuoted());
        return super.toPhysicalTableName(newIdentifier, context);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        return new Identifier(addUnderscores(name.getText()), name.isQuoted());
    }

    protected static String addUnderscores(String name) {
        final StringBuilder buf = new StringBuilder( name.replace('.', '_') );
        for (int i=1; i<buf.length()-1; i++) {
            if (
                    Character.isLowerCase( buf.charAt(i-1) ) &&
                            Character.isUpperCase( buf.charAt(i) ) &&
                            Character.isLowerCase( buf.charAt(i+1) )
            ) {
                buf.insert(i++, '_');
            }
        }
        return buf.toString().toLowerCase(Locale.ROOT);
    }
}