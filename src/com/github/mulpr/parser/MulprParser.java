package com.github.mulpr.parser;

import com.github.mulpr.psi.MulprElements;
import com.intellij.lang.ASTNode;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import org.jetbrains.annotations.NotNull;

public class MulprParser implements PsiParser, MulprElements {

    @NotNull
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder b) {
        doParse(root, b);
        return b.getTreeBuilt();
    }

    @NotNull
    public FlyweightCapableTreeStructure<LighterASTNode> parseLight(IElementType root, PsiBuilder builder) {
        doParse(root, builder);
        return builder.getLightTree();
    }

    private void doParse(@NotNull IElementType root, @NotNull PsiBuilder b) {
        PsiBuilder.Marker fileBlock = b.mark();
        while (!b.eof()) {
            boolean ok = parseProperty(b);
            if (!ok) {
                b.advanceLexer();
            }
        }
        fileBlock.done(root);
    }

    private boolean parseProperty(@NotNull PsiBuilder b) {
        if (b.getTokenType() != KEY_MARKER) {
            return false;
        }
        PsiBuilder.Marker propertyMark = b.mark();
        try {
            b.advanceLexer(); // '~'
            b.advanceLexer(); // key
            b.advanceLexer(); // value: TODO: check if not empty
        } finally {
            propertyMark.done(PROPERTY);
        }
        return true;

    }
}