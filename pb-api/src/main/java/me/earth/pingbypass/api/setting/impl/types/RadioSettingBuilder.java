package me.earth.pingbypass.api.setting.impl.types;

import com.mojang.brigadier.arguments.ArgumentType;
import me.earth.pingbypass.api.command.impl.arguments.DescriptionArgumentTypeImpl;
import me.earth.pingbypass.api.command.impl.arguments.NameableArgumentTypeImpl;
import me.earth.pingbypass.api.config.JsonParser;
import me.earth.pingbypass.api.config.impl.Parsers;
import me.earth.pingbypass.api.registry.Registry;
import me.earth.pingbypass.api.setting.impl.AbstractSettingBuilder;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.function.Function;

public final class RadioSettingBuilder<T extends Nameable> extends AbstractSettingBuilder<T, RadioSetting<T>, RadioSettingBuilder<T>> {
    private final Class<T> type;

    private Function<Registry<T>, ArgumentType<T>> argumentTypeFactory;
    private Function<Registry<T>, JsonParser<T>> parserFactory;
    private LinkedHashSet<T> values = new LinkedHashSet<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public RadioSettingBuilder(Class<T> type) {
        this.type = type;
        withArgumentTypeFactory(registry -> HasDescription.class.isAssignableFrom(type)
                        ? (ArgumentType<T>) new DescriptionArgumentTypeImpl(registry, "value")
                        : new NameableArgumentTypeImpl<>(registry, "value"))
                .withParserFactory(Parsers::registry)
                .withArgumentType(NameableArgumentTypeImpl.empty()) // just to make .verify happy
                .withParser(Parsers.dummy()); // just to make .verify happy
    }

    public RadioSettingBuilder<T> withArgumentTypeFactory(Function<Registry<T>, ArgumentType<T>> argumentTypeFactory) {
        this.argumentTypeFactory = argumentTypeFactory;
        return getSelf();
    }

    public RadioSettingBuilder<T> withParserFactory(Function<Registry<T>, JsonParser<T>> parserFactory) {
        this.parserFactory = parserFactory;
        return getSelf();
    }

    @SafeVarargs
    public final RadioSettingBuilder<T> withValues(T... values) {
        this.values = new LinkedHashSet<>();
        this.values.addAll(Arrays.asList(values));
        if (getDefaultValue() == null) {
            if (values.length > 0) {
                super.withValue(values[0]);
            }
        } else {
            this.values.add(getDefaultValue());
        }

        return getSelf();
    }

    @Override
    protected RadioSetting<T> create() {
        return new RadioSetting<>(getComponentFactory(), getVisibility(), getComplexity(), getConfigType(), getDescription(),
                getDefaultValue(), getName(), type, values, argumentTypeFactory, parserFactory);
    }

    @Override
    public RadioSettingBuilder<T> withValue(T value) {
        values.add(value);
        return super.withValue(value);
    }

}
