package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.ArgumentType;
import lombok.Getter;
import me.earth.pingbypass.api.config.ConfigType;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.registry.impl.OrderedRegistryImpl;
import me.earth.pingbypass.api.setting.Complexity;
import me.earth.pingbypass.api.setting.impl.SettingImpl;
import me.earth.pingbypass.api.traits.CanBeVisible;
import me.earth.pingbypass.api.traits.Nameable;
import net.minecraft.network.chat.Component;

import java.util.LinkedHashSet;
import java.util.function.Function;

/**
 * Similar to an {@link EnumSetting}, but allows values of any type, not only Enums and thus also supports adding values later.
 *
 * @param <T> the type of object handled by this setting.
 */
public class RadioSetting<T extends Nameable> extends SettingImpl<T> {
    @Getter
    private final Registry<T> values;
    private final Class<T> type;

    public RadioSetting(Function<T, Component> componentFactory, CanBeVisible visibility, Complexity complexity,
                         ConfigType configType, String description, T defaultValue, String name, Class<T> type, LinkedHashSet<T> values,
                         Function<Registry<T>, ArgumentType<T>> argumentType, Function<Registry<T>, JsonParser<T>> parser) {
        this(new OrderedRegistryImpl<>(), componentFactory, visibility, complexity, configType, description, defaultValue, name, type, values, argumentType, parser);
    }

    private RadioSetting(Registry<T> registry, Function<T, Component> componentFactory, CanBeVisible visibility, Complexity complexity,
                        ConfigType configType, String description, T defaultValue, String name, Class<T> type, LinkedHashSet<T> values,
                        Function<Registry<T>, ArgumentType<T>> argumentType, Function<Registry<T>, JsonParser<T>> parser) {
        super(componentFactory, visibility, argumentType.apply(registry), complexity, parser.apply(registry), configType, description, defaultValue, name);
        this.type = type;
        this.values = registry;
        values.forEach(this.values::register);
    }

    @Override
    public Class<T> getType() {
        return type;
    }

}
