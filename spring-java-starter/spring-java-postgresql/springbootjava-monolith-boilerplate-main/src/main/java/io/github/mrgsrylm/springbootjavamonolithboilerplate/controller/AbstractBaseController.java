package io.github.mrgsrylm.springbootjavamonolithboilerplate.controller;

import io.github.mrgsrylm.springbootjavamonolithboilerplate.exception.BadRequestException;
import io.github.mrgsrylm.springbootjavamonolithboilerplate.service.MessageSourceService;

import java.util.Arrays;

public abstract class AbstractBaseController {
    /**
     * Sort column check.
     *
     * @param messageSourceService MessageSourceService
     * @param sortColumns          String[]
     * @param sortBy               String
     */
    protected void sortColumnCheck(final MessageSourceService messageSourceService,
                                   final String[] sortColumns,
                                   final String sortBy) {
        if (sortBy != null && !Arrays.asList(sortColumns).contains(sortBy)) {
            throw new BadRequestException(messageSourceService.get("invalid_sort_column"));
        }
    }
}
