/* Generated By:JJTree: Do not edit this line. ASTSeedSpecies.java */

package org.vcell.model.bngl;

public class ASTSeedSpecies extends SimpleNode {

	private String initial;
	private boolean clamped = false;

	public ASTSeedSpecies(int id) {
		super(id);
	}

	public ASTSeedSpecies(BNGLParser p, int id) {
		super(p, id);
	}

	public void setInitial(String image) {
		if (image.startsWith("{") && image.endsWith("}")) {
			image = image.substring(1, image.length()-1);
		}
		this.initial = image;
	}
	public void setClamped() {
		this.clamped = true;
	}

	@Override
	public String toBNGL() {
		return jjtGetChild(0).toBNGL() + " " + initial + "\n";
	}

	/** Accept the visitor. **/
	public Object jjtAccept(BNGLParserVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	public final String getInitial() {
		return initial;
	}
}
