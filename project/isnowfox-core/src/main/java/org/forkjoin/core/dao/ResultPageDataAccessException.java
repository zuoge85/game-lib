package org.forkjoin.core.dao;

import org.springframework.dao.DataAccessException;

public class ResultPageDataAccessException extends DataAccessException {

    public ResultPageDataAccessException(String msg) {
        super(msg);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1984413123405539230L;

}
