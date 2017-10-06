/**
 * Copyright (c) 2017 hujian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.hujian.npc.manager;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by HuJian on 2017/10/3.
 *
 * the rpc response
 */
@Setter @Getter
public class RpcResponse {

    private String requestId; // the request id
    private String error = null; // the error desc
    private Throwable throwable; // the exception
    private Object result; // the result

    public boolean isError() {
        return error != null;
    }

}
