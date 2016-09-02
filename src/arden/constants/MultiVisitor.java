package arden.constants;

import java.util.ArrayList;
import java.util.List;

import arden.constants.analysis.DepthFirstAdapter;
import arden.constants.node.AArdenConstant;
import arden.constants.node.AAtomExpr;
import arden.constants.node.AListExpr;
import arden.constants.node.AListatomExpr;
import arden.runtime.ArdenValue;

class MultiVisitor extends DepthFirstAdapter {
	List<ArdenValue> output = new ArrayList<>();

	@Override
	public void caseAArdenConstant(AArdenConstant node) {
		node.getExpr().apply(this);
	}

	@Override
	public void caseAAtomExpr(AAtomExpr node) {
		// single constant or a list of constants in parentheses
		output.add(ConstantParser.parseValue(node.getAtom()));
	}

	@Override
	public void caseAListatomExpr(AListatomExpr node) {
		// single comma and constant
		output.add(ConstantParser.parseValue(node.getAtom()));
	}

	@Override
	public void caseAListExpr(AListExpr node) {
		// multiple constants, separated by commas
		node.getExpr().apply(this);
		output.add(ConstantParser.parseValue(node.getAtom()));
	}

	public ArdenValue[] getResult() {
		return output.toArray(new ArdenValue[output.size()]);
	}
	
}