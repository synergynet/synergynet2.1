package synergynet.contentsystem.items.implementation.interfaces;

import java.util.List;

import mit.ai.nl.core.Expression;

import synergynet.contentsystem.items.MathPad.MathHandwritingListener;

public interface IMathPadImplementation extends ISketchPadImplementation{
	public void addMathHandwritingListener(MathHandwritingListener listener);
	public void removeHandwritingListener(MathHandwritingListener listener);
	public void removeHandwritingListeners();
	public List<Expression> getMathExpressions();
	public Expression getCurrentExpression();
	public void clearMathExpressions();
	public void startNewExpression();
	public void setMathEngineEnabled(boolean isEnabled);
}
