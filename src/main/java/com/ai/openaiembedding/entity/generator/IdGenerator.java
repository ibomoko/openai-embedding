package com.ai.openaiembedding.entity.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class IdGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}