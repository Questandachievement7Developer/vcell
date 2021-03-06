/* Generated By:JJTree: Do not edit this line. ASTSpeciesPattern.java */

package org.vcell.model.bngl;

public class ASTSpeciesPattern extends SimpleNode {
	public ASTSpeciesPattern(int id) {
		super(id);
	}

	public ASTSpeciesPattern(BNGLParser p, int id) {
		super(p, id);
	}

	public String toBNGL() {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < jjtGetNumChildren(); i++) {
			buffer.append(jjtGetChild(i).toBNGL());
			if (i < jjtGetNumChildren() - 1) {
				buffer.append(".");
			}
		}
		return buffer.toString();
	}

	/** Accept the visitor. **/
	public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}
}
