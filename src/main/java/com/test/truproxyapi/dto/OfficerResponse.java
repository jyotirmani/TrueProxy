package com.test.truproxyapi.dto;

import com.test.truproxyapi.model.Officer;
import lombok.Data;

import java.util.List;

@Data
public class OfficerResponse {
    private List<Officer> officers;
}
