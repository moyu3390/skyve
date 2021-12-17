package modules.test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.io.WKTReader;
import org.skyve.CORE;
import org.skyve.domain.Bean;
import org.skyve.domain.MapBean;
import org.skyve.domain.PersistentMapBean;
import org.skyve.domain.types.DateOnly;
import org.skyve.domain.types.DateTime;
import org.skyve.domain.types.Decimal10;
import org.skyve.domain.types.Decimal2;
import org.skyve.domain.types.Decimal5;
import org.skyve.domain.types.TimeOnly;
import org.skyve.domain.types.Timestamp;
import org.skyve.impl.bind.BindUtil;
import org.skyve.metadata.MetaDataException;
import org.skyve.metadata.model.Attribute;
import org.skyve.metadata.view.TextOutput.Sanitisation;
import org.skyve.util.Binder;
import org.skyve.util.ExpressionEvaluator;
import org.skyve.util.OWASP;
import org.skyve.util.Time;
import org.skyve.util.Util;

import modules.admin.User.UserExtension;
import modules.admin.domain.Contact;
import modules.admin.domain.Snapshot;
import modules.admin.domain.User;
import modules.admin.domain.UserRole;
import modules.test.domain.AllAttributesPersistent;
import modules.test.domain.AllAttributesPersistent.Enum3;

public class BindTests extends AbstractSkyveTest {
	@Test
	@SuppressWarnings("static-method")
	public void testSanitizeBinding() throws Exception {
		Assert.assertNull(BindUtil.sanitiseBinding(null));
		Assert.assertEquals("test", BindUtil.sanitiseBinding("test"));
		Assert.assertEquals("test_test", BindUtil.sanitiseBinding("test.test"));
		Assert.assertEquals("test_test_test", BindUtil.sanitiseBinding("test.test.test"));
		Assert.assertEquals("test1_test2_test3", BindUtil.sanitiseBinding("test1.test2.test3"));
		Assert.assertEquals("test_100__test_test", BindUtil.sanitiseBinding("test[100].test.test"));
		Assert.assertEquals("test_test_0__test", BindUtil.sanitiseBinding("test.test[0].test"));
		Assert.assertEquals("test_100__test_0__test", BindUtil.sanitiseBinding("test[100].test[0].test"));
		Assert.assertEquals("test_100__test_0__test_1_", BindUtil.sanitiseBinding("test[100].test[0].test[1]"));
		Assert.assertEquals("test1_100__test2_0__test3_1_", BindUtil.sanitiseBinding("test1[100].test2[0].test3[1]"));
	}

	@Test
	@SuppressWarnings("static-method")
	public void testUnsanitizeBinding() throws Exception {
		Assert.assertNull(BindUtil.unsanitiseBinding(null));
		Assert.assertEquals("test", BindUtil.unsanitiseBinding("test"));
		Assert.assertEquals("test.test", BindUtil.unsanitiseBinding("test_test"));
		Assert.assertEquals("test.test.test", BindUtil.unsanitiseBinding("test_test_test"));
		Assert.assertEquals("test1.test2.test3", BindUtil.unsanitiseBinding("test1_test2_test3"));
		Assert.assertEquals("test[100].test.test", BindUtil.unsanitiseBinding("test_100__test_test"));
		Assert.assertEquals("test.test[0].test", BindUtil.unsanitiseBinding("test_test_0__test"));
		Assert.assertEquals("test[100].test[0].test", BindUtil.unsanitiseBinding("test_100__test_0__test"));
		Assert.assertEquals("test[100].test[0].test[1]", BindUtil.unsanitiseBinding("test_100__test_0__test_1_"));
		Assert.assertEquals("test1[100].test2[0].test3[1]", BindUtil.unsanitiseBinding("test1_100__test2_0__test3_1_"));
	}
	
	@Test
	@SuppressWarnings("static-method")
	public void testGeneratedJavaIdentifier() throws Exception {
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("1"), "one");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("2"), "two");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("3"), "three");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("4"), "four");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("5"), "five");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("6"), "six");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("7"), "seven");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("8"), "eight");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("9"), "nine");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_1"), "one");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_2"), "two");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_3"), "three");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_4"), "four");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_5"), "five");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_6"), "six");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_7"), "seven");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_8"), "eight");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("_9"), "nine");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("11"), "one1");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("22"), "two2");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("33"), "three3");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("44"), "four4");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("55"), "five5");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("66"), "six6");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("77"), "seven7");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("88"), "eight8");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("99"), "nine9");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v1"), "v1");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v2"), "v2");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v3"), "v3");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v4"), "v4");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v5"), "v5");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v6"), "v6");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v7"), "v7");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v8"), "v8");
		Assert.assertEquals(BindUtil.toJavaInstanceIdentifier("v9"), "v9");
	}
	
	@Test
	public void testCopyProperties() throws Exception {
		AllAttributesPersistent from = Util.constructRandomInstance(u, m, aapd, 2);
		AllAttributesPersistent to = AllAttributesPersistent.newInstance();
		Binder.copy(from, to);
		for (Attribute a: aapd.getAttributes()) {
			String name = a.getName();
				Assert.assertEquals("Property Name " + name, Binder.get(from, name), Binder.get(to, name));
		}
	}
	
	@Test
	@SuppressWarnings("static-method")
	public void testCopyPropertiesWithChildCollection() throws Exception {
		UserExtension from = User.newInstance();
		UserRole role = UserRole.newInstance();
		from.getRoles().add(role);
		role.setParent(from);
		User to = User.newInstance();
		Binder.copy(from, to);
		Assert.assertEquals(role.getParent(), to);
	}
	
	@Test
	@SuppressWarnings("static-method")
	public void testSimpleMapBeanProperty() {
		Map<String, Object> map = new TreeMap<>();
		map.put(User.userNamePropertyName, "mike");
		MapBean bean = new MapBean(User.MODULE_NAME, User.DOCUMENT_NAME, map);
		Assert.assertEquals("mike", Binder.get(bean, User.userNamePropertyName)); 
	}

	@Test
	@SuppressWarnings("static-method")
	public void testCompoundMapBeanProperty() {
		String binding = Binder.createCompoundBinding(User.contactPropertyName, Contact.namePropertyName);
		Map<String, Object> map = new TreeMap<>();
		map.put(binding, "mike");
		MapBean bean = new MapBean(User.MODULE_NAME, User.DOCUMENT_NAME, map);
		Assert.assertEquals("mike", Binder.get(bean, binding)); 
	}
	
	@Test
	public void testSimpleThisProperty() throws Exception {
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(MapBean.BEAN_PROPERTY_KEY, aap);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertTrue(Binder.get(bean, AllAttributesPersistent.booleanFlagPropertyName) instanceof Boolean);
	}
	
	@Test
	public void testCompoundThisProperty() throws Exception {
		String binding = Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.booleanFlagPropertyName);
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(MapBean.BEAN_PROPERTY_KEY, aap);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertTrue(Binder.get(bean, binding) instanceof Boolean);
	}

	@Test
	public void testSimpleMapPropertyOverThisProperty() throws Exception {
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(MapBean.BEAN_PROPERTY_KEY, aap);
		map.put(AllAttributesPersistent.booleanFlagPropertyName, null);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertFalse(Binder.get(bean, AllAttributesPersistent.booleanFlagPropertyName) instanceof Boolean);
	}

	@Test
	public void testCompoundMapPropertyOverThisProperty() throws Exception {
		String binding = Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.booleanFlagPropertyName);
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(MapBean.BEAN_PROPERTY_KEY, aap);
		map.put(binding, null);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertFalse(Binder.get(bean, binding) instanceof Boolean);
	}
	
	@Test
	public void testCompoundPropertyWithoutMappedProperty2Deep() throws Exception {
		String binding = Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.booleanFlagPropertyName);
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(AllAttributesPersistent.aggregatedAssociationPropertyName, aap);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertTrue(Binder.get(bean, binding) instanceof Boolean);
	}

	@Test
	public void testCompoundPropertyWithoutMappedProperty3Deep() throws Exception {
		String binding = Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.booleanFlagPropertyName);
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(AllAttributesPersistent.aggregatedAssociationPropertyName, aap);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertTrue(Binder.get(bean, binding) instanceof Boolean);
	}

	@Test
	public void testCompoundPropertyWithoutMappedProperty4Deep() throws Exception {
		String binding = Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.aggregatedAssociationPropertyName,
														AllAttributesPersistent.booleanFlagPropertyName);
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Map<String, Object> map = new TreeMap<>();
		map.put(Binder.createCompoundBinding(AllAttributesPersistent.aggregatedAssociationPropertyName,
												AllAttributesPersistent.aggregatedAssociationPropertyName),
					aap);
		MapBean bean = new MapBean(m.getName(), aapd.getName(), map);
		Assert.assertTrue(Binder.get(bean, binding) instanceof Boolean);
	}
	
	@Test
	public void testFormatMessage() throws Exception {
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		aap.setText("Test");
		Assert.assertEquals("Test", Binder.formatMessage("Test", aap));
		Assert.assertEquals("Test", Binder.formatMessage("{text}", aap));
		Assert.assertEquals("TestTest", Binder.formatMessage("{text}{text}", aap));
		// Test escapes
		Assert.assertEquals("{text}Test", Binder.formatMessage("\\{text\\}{text}", aap));
		Assert.assertEquals("{textTest", Binder.formatMessage("\\{text{text}", aap));
		Assert.assertEquals("text}Test", Binder.formatMessage("text\\}{text}", aap));
		Assert.assertEquals("{text", Binder.formatMessage("\\{text", aap));
		aap.setText("{text}");
		// Test '{' and '}' are left in tact when they are part of the value substituted
		Assert.assertEquals("{text}{text}", Binder.formatMessage("{text}{text}", aap));
	}
	
	@Test(expected = MetaDataException.class)
	public void testDanglingMessageFormat() throws Exception {
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		Assert.assertEquals("{text", Binder.formatMessage("{text", aap));
	}
	
	@Test
	public void testExpressions() throws Exception {
		AllAttributesPersistent bean = Util.constructRandomInstance(u, m, aapd, 2);
		bean.setText("Test");
		CORE.getStash().put("text", "Stash");
		CORE.getUser().getAttributes().put("text", "Attribute");
		
		final String currentDate = CORE.getCustomer().getDefaultDateConverter().toDisplayValue(new DateOnly());
		final String tomorrowsDate = CORE.getCustomer().getDefaultDateConverter().toDisplayValue(Time.addDaysToNew(new DateOnly(), 1));
		final String currentTime = CORE.getCustomer().getDefaultTimeConverter().toDisplayValue(new TimeOnly());
		final String currentDateTime = CORE.getCustomer().getDefaultDateTimeConverter().toDisplayValue(new DateTime());
		final String currentTimestamp = CORE.getCustomer().getDefaultTimestampConverter().toDisplayValue(new Timestamp());
		
		Assert.assertEquals("TestUser", Binder.formatMessage("{USER}", bean));
		Assert.assertEquals("TestUser", Binder.formatMessage("{USERID}", bean));
		Assert.assertEquals("", Binder.formatMessage("{USERNAME}", bean));
		Assert.assertEquals("", Binder.formatMessage("{DATAGROUPID}", bean));
		Assert.assertNotEquals("", Binder.formatMessage("{CONTACTID}", bean));
		Assert.assertEquals("bizhub", Binder.formatMessage("{CUSTOMER}", bean));
		Assert.assertEquals(currentDate, Binder.formatMessage("{DATE}", bean));
		Assert.assertEquals(currentTime, Binder.formatMessage("{TIME}", bean));
		Assert.assertEquals(currentDateTime, Binder.formatMessage("{DATETIME}", bean));
		Assert.assertEquals(currentTimestamp, Binder.formatMessage("{TIMESTAMP}", bean));
		Assert.assertEquals(Util.getDocumentUrl(bean), Binder.formatMessage("{URL}", bean));
		
		Assert.assertEquals("Test", Binder.formatMessage("{text}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{ text }", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{bean:text}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{bean: text }", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{bean : text }", bean));
		Assert.assertEquals("Text", Binder.formatMessage("{disp:text}", bean));
		Assert.assertEquals("", Binder.formatMessage("{desc:text}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{el:bean.text}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{el:bean.text}", bean));
		Assert.assertEquals("Stash", Binder.formatMessage("{el:stash['text']}", bean));
		Assert.assertEquals("Attribute", Binder.formatMessage("{el:user.attributes['text']}", bean));
		Assert.assertEquals("", Binder.formatMessage("{el:stash['nothing']}", bean));
		Assert.assertEquals("", Binder.formatMessage("{el:user.attributes['nothing']}", bean));

		Assert.assertEquals("some.non-existent.key", Binder.formatMessage("{i18n:some.non-existent.key}", bean));
		Assert.assertEquals("Yes", Binder.formatMessage("{role:admin.BasicUser}", bean));
		Assert.assertEquals("Stash", Binder.formatMessage("{stash:text}", bean));
		Assert.assertEquals("", Binder.formatMessage("{stash:nothing}", bean));
		Assert.assertEquals("Attribute", Binder.formatMessage("{user:text}", bean));
		Assert.assertEquals("", Binder.formatMessage("{user:nothing}", bean));
		
		// Test functions and imports
		Assert.assertEquals(currentDate, Binder.formatMessage("{el:newDateOnly()}", bean));
		Assert.assertEquals(tomorrowsDate, Binder.formatMessage("{el:newDateOnlyFromLocalDate(newDateOnly().toLocalDate().plusDays(1))}", bean));
		Assert.assertEquals(currentTime, Binder.formatMessage("{el:newTimeOnly()}", bean));
		Assert.assertEquals(currentDateTime, Binder.formatMessage("{el:newDateTime()}", bean));
		Assert.assertEquals(currentTimestamp, Binder.formatMessage("{el:newTimestamp()}", bean));
		Assert.assertEquals("0.00", Binder.formatMessage("{el:Decimal2.ZERO}", bean));
		Assert.assertEquals("100.00", Binder.formatMessage("{el:newDecimal2(100)}", bean));
	}
	
	@Test
	public void testDynamicExpressions() throws Exception {
		PersistentMapBean bean = Util.constructRandomInstance(u, m, aadpd, 2);
		Binder.set(bean, AllAttributesPersistent.textPropertyName, "Test");
		
		Assert.assertEquals("Test", Binder.formatMessage("{text}", bean));
		Assert.assertEquals(Boolean.FALSE, ExpressionEvaluator.evaluate("{condition}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{bean:text}", bean));
		Assert.assertEquals("Test", Binder.formatMessage("{el:bean.text}", bean));
		Assert.assertEquals(Boolean.FALSE, ExpressionEvaluator.evaluate("{el:bean.condition}", bean));
		
		bean = aadpd.newInstance(u);
		System.out.println(bean);
		System.out.println();
	}
	
	@Test
	public void testDynamicDefaults() throws Exception {
		Bean bean = aadpd.newInstance(u);
		Assert.assertEquals(Boolean.TRUE, Binder.get(bean, AllAttributesPersistent.booleanFlagPropertyName));
		Assert.assertEquals("#000000", Binder.get(bean, AllAttributesPersistent.colourPropertyName));
		Assert.assertEquals(new DateOnly("2021-10-21"), Binder.get(bean, AllAttributesPersistent.datePropertyName));
		Assert.assertEquals(new DateTime("2021-10-21T07:48:29Z"), Binder.get(bean, AllAttributesPersistent.dateTimePropertyName));
		Assert.assertEquals(new Decimal10(100.1234567899), Binder.get(bean, AllAttributesPersistent.decimal10PropertyName));
		Assert.assertEquals(new Decimal2(100.12), Binder.get(bean, AllAttributesPersistent.decimal2PropertyName));
		Assert.assertEquals(new Decimal5(100.12345), Binder.get(bean, AllAttributesPersistent.decimal5PropertyName));
		// NB This can't be a real enum value coz there is no generated class
		Assert.assertEquals("one", Binder.get(bean, AllAttributesPersistent.enum3PropertyName));
		// NB This is a real enum value coz its a dynamic reference to a generated class
		Assert.assertEquals(Enum3.one, Binder.get(bean, "enum3Reference"));
		Assert.assertEquals(new WKTReader().read("POINT(0 0)"), Binder.get(bean, AllAttributesPersistent.geometryPropertyName));
		Assert.assertEquals("1234567890", Binder.get(bean, AllAttributesPersistent.idPropertyName));
		Assert.assertEquals(Integer.valueOf(123), Binder.get(bean,  AllAttributesPersistent.normalIntegerPropertyName));
		Assert.assertEquals(Long.valueOf(123), Binder.get(bean,  AllAttributesPersistent.longIntegerPropertyName));
		Assert.assertEquals("<h1>Markup</h1>", Binder.get(bean, AllAttributesPersistent.markupPropertyName));
		Assert.assertEquals("Memo", Binder.get(bean, AllAttributesPersistent.memoPropertyName));
		Assert.assertEquals("Text", Binder.get(bean, AllAttributesPersistent.textPropertyName));
		Assert.assertEquals(new TimeOnly("07:51:26"), Binder.get(bean, AllAttributesPersistent.timePropertyName));
		Assert.assertEquals(new Timestamp("2021-10-21T07:48:29Z"), Binder.get(bean, AllAttributesPersistent.timestampPropertyName));
		
		bean = aapd.newInstance(u);
		Assert.assertEquals(Boolean.FALSE, Binder.get(bean, AllAttributesPersistent.booleanFlagPropertyName));
		Assert.assertEquals("#000000", Binder.get(bean, AllAttributesPersistent.colourPropertyName));
		Assert.assertEquals(new DateOnly(), Binder.get(bean, AllAttributesPersistent.datePropertyName));
		Assert.assertEquals(new DateTime(), Binder.get(bean, AllAttributesPersistent.dateTimePropertyName));
		Assert.assertEquals(Decimal10.ZERO, Binder.get(bean, AllAttributesPersistent.decimal10PropertyName));
		Assert.assertEquals(Decimal2.ZERO, Binder.get(bean, AllAttributesPersistent.decimal2PropertyName));
		Assert.assertEquals(Decimal5.ZERO, Binder.get(bean, AllAttributesPersistent.decimal5PropertyName));
		Assert.assertEquals(Enum3.one, Binder.get(bean, AllAttributesPersistent.enum3PropertyName));
		Assert.assertEquals(new WKTReader().read("POINT(0 0)"), Binder.get(bean, AllAttributesPersistent.geometryPropertyName));
		Assert.assertEquals("Test POINT (0 0)", Binder.get(bean, AllAttributesPersistent.memoPropertyName));
	}
	
	
	@Test
	public void testExpressionValidation() throws Exception {
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{USER}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{USERID}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{USERNAME}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{DATAGROUPID}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{CONTACTID}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{CUSTOMER}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{DATE}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{TIME}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{DATETIME}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{TIMESTAMP}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{URL}"));
		
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{ text }"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{bean:text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{bean: text }"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{bean : text }"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{disp:text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{desc:text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:bean.doesNotExist}"));
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.doesNotExist}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:bean..doesNotExist}"));
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean..doesNotExist}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:stash['text']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stash['text']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:user.attributes['text']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:user.attributes['text']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:stash['nothing']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stash['nothing']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:user.attributes['nothing']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:user.attributes['nothing']}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{i18n:some.non-existent.key}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{role:admin.BasicUser}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{stash:text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{stash:nothing}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{user:text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{user:nothing}"));
		
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "}"));
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{}"));
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{text\\}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "\\{text}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "\\{{text}"));
		
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.normalInteger + bean.normalInteger}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.longInteger + bean.longInteger}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.decimal2.bigDecimalValue() + bean.decimal2.bigDecimalValue()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.decimal5.add(bean.decimal5)}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.date == bean.date}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:empty bean.date}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:not empty bean.date and bean.booleanFlag}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:empty bean.date ? DATE : bean.date}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:bean.text.concat(bean.text)}"));

		// Stash operation puts that expression evaluation into typeless mode
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stash['key'].concat(bean.text)}"));
		// Stashy is still a problem though
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stashy['key'].concat(bean.text)}"));
		// And the other expression for the concat parameter is still validated
		Assert.assertNotNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stash['key'].concat(bean.texty)}"));
		// But concaty method name is not chacked coz we lost the type information retrieving from the stash
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:stash['key'].concaty(bean.text)}"));

		Assert.assertNull(ExpressionEvaluator.validate("{el:bean.aggregatedAssociation}", Bean.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{el:empty bean.aggregatedAssociation}", Boolean.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{el:bean.aggregatedCollection}", List.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{el:bean.date}", DateOnly.class, c, m, aapd));

		Assert.assertNull(ExpressionEvaluator.validate("{aggregatedAssociation}", Bean.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{bizId}", String.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{aggregatedAssociation.bizId}", String.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{aggregatedCollection}", List.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{date}", DateOnly.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{condition}", Boolean.class, c, m, aapd));
		Assert.assertNull(ExpressionEvaluator.validate("{aggregatedAssociation.condition}", Boolean.class, c, m, aapd));

		// Test functions
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:newDateOnly()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newDateOnly()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:newDateOnlyFromLocalDate(newDateOnly().toLocalDate().plusDays(1))}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newDateOnlyFromLocalDate(newDateOnly().toLocalDate().plusDays(1))}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:newTimeOnly()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newTimeOnly()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:newDateTime()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newDateTime()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{rtel:newTimestamp()}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newTimestamp()}"));
		
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:Decimal2.ZERO}"));
		Assert.assertNull(BindUtil.validateMessageExpressions(c, m, aapd, "{el:newDecimal2(100)}"));
	}
	
	/**
	 * Test that a MapBean with a display binding with a dynamic domain defined by no THIS_ALIAS returns code.
	 */
	@Test
	public void testMapBeanWithNoThisReturnsDynamicDomainCode() {
		Map<String, Object> properties = new TreeMap<>();
		properties.put(Snapshot.queryNamePropertyName, "dynamicDomainValue");
		MapBean bean = new MapBean(Snapshot.MODULE_NAME, Snapshot.DOCUMENT_NAME, properties);
		Assert.assertEquals("Dynamic domain code value with no THIS_ALIAS should return the code value for a MapBean",
								"dynamicDomainValue",
								BindUtil.getDisplay(c, bean, Snapshot.queryNamePropertyName));
	}
	
	@Test
	public void testSanitiseAsFunction() throws Exception {
		AllAttributesPersistent aap = Util.constructRandomInstance(u, m, aapd, 2);
		aap.setText("Test<script>alert(1)</script>Me");

		Assert.assertEquals("Format Message with sanitise function should remove script tag", 
								"<h1>TestMe</h1>",
								BindUtil.formatMessage("<h1>{text}</h1>", displayName -> OWASP.sanitise(Sanitisation.relaxed, displayName), aap));
	}

}
