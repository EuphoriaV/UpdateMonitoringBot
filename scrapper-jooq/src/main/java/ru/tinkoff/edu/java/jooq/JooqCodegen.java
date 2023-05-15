package ru.tinkoff.edu.java.jooq;

import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Jdbc;
import org.jooq.meta.jaxb.Target;

public class JooqCodegen {
    public static void main(String[] args) throws Exception {
        Target target = new Target()
            .withPackageName("ru.tinkoff.edu.java.scrapper.domain.jooq")
            .withDirectory("scrapper/src/main/java");

        Configuration configuration = new Configuration()
            .withJdbc(
                new Jdbc()
                    .withDriver("org.postgresql.Driver")
                    .withUrl("jdbc:postgresql://localhost:5432/scrapper")
                    .withUsername("EuphoriaV")
                    .withPassword("1234")
            )
            .withGenerator(
                new Generator()
                    .withDatabase(new Database()
                        .withName("org.jooq.meta.postgres.PostgresDatabase")
                        .withIncludes(".*")
                        .withExcludes("")
                        .withInputSchema("public"))
                    .withTarget(target)
            );

        GenerationTool.generate(configuration);
    }
}
