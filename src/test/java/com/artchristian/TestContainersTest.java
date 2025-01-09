package com.artchristian;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class TestContainersTest extends AbstractTestContainer {

    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}
