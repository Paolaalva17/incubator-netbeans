/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.javascript2.extdoc;

import java.util.Collections;
import org.netbeans.modules.javascript2.doc.JsDocumentationTestBase;
import org.netbeans.modules.javascript2.doc.spi.JsDocumentationHolder;
import org.netbeans.modules.javascript2.editor.parser.JsParserResult;
import org.netbeans.modules.parsing.api.ParserManager;
import org.netbeans.modules.parsing.api.ResultIterator;
import org.netbeans.modules.parsing.api.Source;
import org.netbeans.modules.parsing.api.UserTask;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.Parser;

/**
 *
 * @author Martin Fousek <marfous@netbeans.org>
 */
public class ExtDocCompletionDocumentationTest extends JsDocumentationTestBase {

    private static final String BASE_PATH = "testfiles/extdoc/completionDocumentation/";

    private JsDocumentationHolder documentationHolder;
    private JsParserResult parserResult;

    public ExtDocCompletionDocumentationTest(String testName) {
        super(testName);
    }

    private void initializeDocumentationHolder(Source source) throws ParseException {
        ParserManager.parse(Collections.singleton(source), new UserTask() {
            public @Override void run(ResultIterator resultIterator) throws Exception {
                Parser.Result result = resultIterator.getParserResult();
                assertTrue(result instanceof JsParserResult);
                parserResult = (JsParserResult) result;
                documentationHolder = getDocumentationHolder(parserResult, new ExtDocDocumentationProvider());
            }
        });
    }

    private void checkCompletionDocumentation(String relPath, String caretSeeker) throws Exception {
        Source testSource = getTestSource(getTestFile(relPath));
        final int caretOffset = getCaretOffset(testSource, caretSeeker);
        initializeDocumentationHolder(testSource);
        assertDescriptionMatches(relPath, documentationHolder.getDocumentation(getNodeForOffset(parserResult, caretOffset)).getContent(), true, "completionDoc.html");
    }

    public void testCompletionDocumentation01() throws Exception {
        checkCompletionDocumentation(BASE_PATH + "completionDocumentation01.js", "function Shape()^{");
    }

    public void testCompletionDocumentation02() throws Exception {
        checkCompletionDocumentation(BASE_PATH + "completionDocumentation01.js", "function addReference()^{");
    }

    public void testCompletionDocumentation03() throws Exception {
        checkCompletionDocumentation(BASE_PATH + "completionDocumentation01.js", "function Hexagon(sideLength) ^{");
    }

    public void testCompletionDocumentation04() throws Exception {
        checkCompletionDocumentation(BASE_PATH + "completionDocumentation01.js", "function Circle(radius)^{");
    }

    public void testCompletionDocumentation05() throws Exception {
        // check extends
        checkCompletionDocumentation(BASE_PATH + "completionDocumentation01.js", "function Triangle() ^{");
    }

}
