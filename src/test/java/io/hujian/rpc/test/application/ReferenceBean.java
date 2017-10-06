package io.hujian.rpc.test.application;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by hujian06 on 2017/10/6.
 *
 * the test bean
 */
@Setter
@Getter
public class ReferenceBean {

    private String desc;
    private Class<Object> clazz;

}
