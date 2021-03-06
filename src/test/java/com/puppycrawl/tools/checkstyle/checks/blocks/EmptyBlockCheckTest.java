////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2016 the original author or authors.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////

package com.puppycrawl.tools.checkstyle.checks.blocks;

import static com.puppycrawl.tools.checkstyle.checks.blocks.EmptyBlockCheck.MSG_KEY_BLOCK_EMPTY;
import static com.puppycrawl.tools.checkstyle.checks.blocks.EmptyBlockCheck.MSG_KEY_BLOCK_NO_STMT;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.BaseCheckTestSupport;
import com.puppycrawl.tools.checkstyle.DefaultConfiguration;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.utils.CommonUtils;

public class EmptyBlockCheckTest
    extends BaseCheckTestSupport {
    @Override
    protected String getPath(String filename) throws IOException {
        return super.getPath("checks" + File.separator
                + "blocks" + File.separator + filename);
    }

    /* Additional test for jacoco, since valueOf()
     * is generated by javac and jacoco reports that
     * valueOf() is uncovered.
     */
    @Test
    public void testBlockOptionValueOf() {
        final BlockOption option = BlockOption.valueOf("TEXT");
        assertEquals(BlockOption.TEXT, option);
    }

    @Test
    public void testDefault()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(EmptyBlockCheck.class);
        final String[] expected = {
            "33:13: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "35:17: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "37:13: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "40:17: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "63:5: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "71:29: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "73:41: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "84:12: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
        };
        verify(checkConfig, getPath("InputSemantic.java"), expected);
    }

    @Test
    public void testText()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(EmptyBlockCheck.class);
        checkConfig.addAttribute("option", BlockOption.TEXT.toString());
        final String[] expected = {
            "33:13: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "try"),
            "35:17: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "finally"),
            "63:5: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "INSTANCE_INIT"),
            "71:29: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "synchronized"),
            "84:12: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "STATIC_INIT"),
        };
        verify(checkConfig, getPath("InputSemantic.java"), expected);
    }

    @Test
    public void testStatement()
            throws Exception {
        final DefaultConfiguration checkConfig =
            createCheckConfig(EmptyBlockCheck.class);
        checkConfig.addAttribute("option", BlockOption.STMT.toString());
        final String[] expected = {
            "33:13: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "35:17: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "37:13: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "40:17: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "63:5: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "71:29: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "73:41: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "84:12: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
        };
        verify(checkConfig, getPath("InputSemantic.java"), expected);
    }

    @Test
    public void allowEmptyLoops() throws Exception {
        final DefaultConfiguration checkConfig =
                createCheckConfig(EmptyBlockCheck.class);
        checkConfig.addAttribute("option", BlockOption.STMT.toString());
        checkConfig.addAttribute("tokens", "LITERAL_TRY, LITERAL_CATCH,"
                + "LITERAL_FINALLY, LITERAL_DO, LITERAL_IF,"
                + "LITERAL_ELSE, INSTANCE_INIT, STATIC_INIT, LITERAL_SWITCH");
        final String[] expected = {
            "16:29: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "19:42: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "22:29: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
            "23:28: " + getCheckMessage(MSG_KEY_BLOCK_NO_STMT),
        };
        verify(checkConfig, getPath("InputSemantic2.java"), expected);
    }

    @Test
    public void allowEmptyLoopsText() throws Exception {
        final DefaultConfiguration checkConfig =
                createCheckConfig(EmptyBlockCheck.class);
        checkConfig.addAttribute("option", BlockOption.TEXT.toString());
        checkConfig.addAttribute("tokens", "LITERAL_TRY, LITERAL_CATCH,"
                + "LITERAL_FINALLY, LITERAL_DO, LITERAL_IF,"
                + "LITERAL_ELSE, INSTANCE_INIT, STATIC_INIT, LITERAL_SWITCH");
        final String[] expected = {
            "16:29: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "if"),
            "19:42: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "if"),
            "22:29: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "if"),
            "23:28: " + getCheckMessage(MSG_KEY_BLOCK_EMPTY, "switch"),
        };
        verify(checkConfig, getPath("InputSemantic2.java"), expected);
    }

    @Test(expected = CheckstyleException.class)
    public void testInvalidOption() throws Exception {
        final DefaultConfiguration checkConfig = createCheckConfig(EmptyBlockCheck.class);
        checkConfig.addAttribute("option", "invalid_option");
        final String[] expected = CommonUtils.EMPTY_STRING_ARRAY;

        verify(checkConfig, getPath("InputSemantic.java"), expected);
    }
}
