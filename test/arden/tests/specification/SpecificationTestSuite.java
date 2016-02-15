package arden.tests.specification;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import arden.tests.specification.Categories.KnowledgeCategoryTest;
import arden.tests.specification.Categories.LibraryCategoryTest;
import arden.tests.specification.Categories.MaintenanceCategoryTest;
import arden.tests.specification.Operators.AggregationOperatorsTest;
import arden.tests.specification.Operators.ArithmeticOperatorsTest;
import arden.tests.specification.Operators.DurationOperatorsTest;
import arden.tests.specification.Operators.GeneralPropertiesTest;
import arden.tests.specification.Operators.IsComparisonOperatorsTest;
import arden.tests.specification.Operators.ListOperatorsTest;
import arden.tests.specification.Operators.LogicalOperatorsTest;
import arden.tests.specification.Operators.NumericFunctionOperatorsTest;
import arden.tests.specification.Operators.ObjectOperatorsTest;
import arden.tests.specification.Operators.OccurComparisonOperatorsTest;
import arden.tests.specification.Operators.QueryAggregationOperatorsTest;
import arden.tests.specification.Operators.QueryTransformationOperatorsTest;
import arden.tests.specification.Operators.SimpleComparisonOperatorsTest;
import arden.tests.specification.Operators.StringOperatorsTest;
import arden.tests.specification.Operators.TemporalOperatorsTest;
import arden.tests.specification.Operators.TimeFunctionOperatorsTest;
import arden.tests.specification.Operators.TransformationOperatorsTest;
import arden.tests.specification.Operators.WhereOperatorTest;
import arden.tests.specification.StructureSlots.ActionSlotTest;
import arden.tests.specification.StructureSlots.DataSlotTest;
import arden.tests.specification.StructureSlots.EvokeSlotTest;
import arden.tests.specification.StructureSlots.LogicSlotTest;
import arden.tests.specification.StructureSlots.OrganizationTest;
import arden.tests.specification.StructureSlots.TokensTest;

@RunWith(Suite.class)
@SuiteClasses({
	KnowledgeCategoryTest.class,
	LibraryCategoryTest.class,
	MaintenanceCategoryTest.class,
	AggregationOperatorsTest.class,
	ArithmeticOperatorsTest.class,
	DurationOperatorsTest.class,
	GeneralPropertiesTest.class,
	IsComparisonOperatorsTest.class,
	ListOperatorsTest.class,
	LogicalOperatorsTest.class,
	NumericFunctionOperatorsTest.class,
	ObjectOperatorsTest.class,
	OccurComparisonOperatorsTest.class,
	QueryAggregationOperatorsTest.class,
	QueryTransformationOperatorsTest.class,
	SimpleComparisonOperatorsTest.class,
	StringOperatorsTest.class,
	TemporalOperatorsTest.class,
	TimeFunctionOperatorsTest.class,
	TransformationOperatorsTest.class,
	WhereOperatorTest.class,
	ActionSlotTest.class,
	DataSlotTest.class,
	EvokeSlotTest.class,
	LogicSlotTest.class,
	OrganizationTest.class,
	TokensTest.class,
	DataTypesTest.class,
	MlmFormatTest.class
})
public class SpecificationTestSuite {
}
