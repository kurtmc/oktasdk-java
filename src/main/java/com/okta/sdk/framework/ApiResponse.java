/*!
 * Copyright (c) 2015-2016, Okta, Inc. and/or its affiliates. All rights reserved.
 * The Okta software accompanied by this notice is provided pursuant to the Apache License, Version 2.0 (the "License.")
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.okta.sdk.framework;

import com.okta.sdk.models.links.Link;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiResponse<T> {

    private HttpResponse response;
    private T responseObject;

    public ApiResponse(HttpResponse response, T responseObject) {
        this.response = response;
        this.responseObject = responseObject;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public Header getHeader(String name) {
        return response.getFirstHeader(name);
    }

    public Map<String, Link> getLinks() {
        Header[] headers = response.getHeaders("Link");
        Map<String, Link> links = new HashMap<String, Link>();
        Pattern linkPattern = Pattern.compile("<(.*)>;\\s+rel=\"(.*)\"");
        for (Header header : headers) {
            Matcher m = linkPattern.matcher(header.getValue());
            if (m.matches()) {
                Link link = new Link();

                // Get the matching groups
                // https://www.debuggex.com/r/ln-9bu5wy6Km-WWG

                // Group 0 is the entire string

                // Group 1 is the URL
                link.setHref(m.group(1));

                // Group 2 is the relation
                String rel = m.group(2);

                links.put(rel, link);
            }
        }
        return links;
    }
}
