package com.lomoye.easy;

import com.lomoye.easy.backend.ImportExampleService;
import com.lomoye.easy.backend.JobBackendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobEntry {
    @Autowired
    private JobBackendService jobBackendService;

    @Autowired
    private ImportExampleService importExampleService;

    public void start() throws Exception {
        jobBackendService.start();

        importExampleService.start();
    }
}
