package com.bvp.task;

import com.bvp.task.config.AppConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppConfigTest {

    @Test
    public void testModelMapperBean() {
        assertNotNull(new AppConfig().modelMapper());
    }
}
