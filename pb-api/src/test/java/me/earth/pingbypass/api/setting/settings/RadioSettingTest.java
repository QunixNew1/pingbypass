package me.earth.pingbypass.api.setting.settings;

import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.earth.pingbypass.api.config.ConfigParseException;
import me.earth.pingbypass.api.setting.impl.SettingRegistryImpl;
import me.earth.pingbypass.api.setting.impl.types.RegistersSettingTypes;
import me.earth.pingbypass.api.traits.Nameable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RadioSettingTest {
    @Test
    public void testCreate() throws CommandSyntaxException {
        var settingRegistry = new SettingRegistryRegisteringSettings();
        var builder = settingRegistry.radioBuilder("Test", NameableValue.class, "TestDescription", NameableValue.VALUE_1, NameableValue.VALUE_3);
        var radioSetting = builder.build();
        assertEquals("Test", radioSetting.getName());
        assertEquals(NameableValue.class, radioSetting.getType());
        assertEquals("TestDescription", radioSetting.getDescription());
        assertEquals(NameableValue.VALUE_1, radioSetting.getDefaultValue());
        assertEquals(NameableValue.VALUE_1, radioSetting.getValue());
        assertTrue(radioSetting.getValues().contains("1"));
        assertFalse(radioSetting.getValues().contains("2"));
        assertTrue(radioSetting.getValues().contains("3"));
        radioSetting.fromJson(new JsonPrimitive("3"));
        assertEquals(NameableValue.VALUE_3, radioSetting.getValue());
        assertThrows(ConfigParseException.class, () -> radioSetting.fromJson(new JsonPrimitive("2")));
        radioSetting.getValues().register(NameableValue.VALUE_2);
        radioSetting.fromJson(new JsonPrimitive("2"));
        assertEquals(NameableValue.VALUE_2, radioSetting.getValue());
        assertEquals(NameableValue.VALUE_2, radioSetting.getArgumentType().parse(new StringReader("2")));
    }

    @Getter
    @RequiredArgsConstructor
    private enum NameableValue implements Nameable {
        VALUE_1("1"),
        VALUE_2("2"),
        VALUE_3("3");

        private final String name;
    }

    private static final class SettingRegistryRegisteringSettings extends SettingRegistryImpl implements RegistersSettingTypes {

    }

}
