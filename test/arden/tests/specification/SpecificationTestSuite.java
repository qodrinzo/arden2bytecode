package arden.tests.specification;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import arden.tests.specification.categories.KnowledgeCategoryTest;
import arden.tests.specification.categories.LibraryCategoryTest;
import arden.tests.specification.categories.MaintenanceCategoryTest;
import arden.tests.specification.categories.ResourcesCategoryTest;
import arden.tests.specification.operators.AggregationOperatorsTest;
import arden.tests.specification.operators.ArithmeticOperatorsTest;
import arden.tests.specification.operators.DurationOperatorsTest;
import arden.tests.specification.operators.FuzzyOperatorsTest;
import arden.tests.specification.operators.GeneralPropertiesTest;
import arden.tests.specification.operators.IsComparisonOperatorsTest;
import arden.tests.specification.operators.ListOperatorsTest;
import arden.tests.specification.operators.LogicalOperatorsTest;
import arden.tests.specification.operators.NumericFunctionOperatorsTest;
import arden.tests.specification.operators.ObjectOperatorsTest;
import arden.tests.specification.operators.OccurComparisonOperatorsTest;
import arden.tests.specification.operators.QueryAggregationOperatorsTest;
import arden.tests.specification.operators.QueryTransformationOperatorsTest;
import arden.tests.specification.operators.SimpleComparisonOperatorsTest;
import arden.tests.specification.operators.StringOperatorsTest;
import arden.tests.specification.operators.TemporalOperatorsTest;
import arden.tests.specification.operators.TimeFunctionOperatorsTest;
import arden.tests.specification.operators.TransformationOperatorsTest;
import arden.tests.specification.operators.TypeConversionOperatorsTest;
import arden.tests.specification.operators.WhereOperatorTest;
import arden.tests.specification.structureslots.ActionSlotTest;
import arden.tests.specification.structureslots.DataSlotTest;
import arden.tests.specification.structureslots.EvokeSlotTest;
import arden.tests.specification.structureslots.LogicSlotTest;
import arden.tests.specification.structureslots.OrganizationTest;
import arden.tests.specification.structureslots.TokensTest;

@RunWith(Suite.class)
@SuiteClasses({
	KnowledgeCategoryTest.class,
	LibraryCategoryTest.class,
	MaintenanceCategoryTest.class,
	ResourcesCategoryTest.class,
	AggregationOperatorsTest.class,
	ArithmeticOperatorsTest.class,
	DurationOperatorsTest.class,
	FuzzyOperatorsTest.class,
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
	TypeConversionOperatorsTest.class,
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
