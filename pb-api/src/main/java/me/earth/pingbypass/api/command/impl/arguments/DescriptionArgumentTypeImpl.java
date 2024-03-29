package me.earth.pingbypass.api.command.impl.arguments;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.earth.pingbypass.api.traits.HasDescription;
import me.earth.pingbypass.api.traits.Nameable;
import me.earth.pingbypass.api.traits.Streamable;

import java.util.concurrent.CompletableFuture;

public class DescriptionArgumentTypeImpl<T extends Nameable & HasDescription> extends NameableArgumentTypeImpl<T> implements DescriptionArgumentType<T> {
    public DescriptionArgumentTypeImpl(Streamable<T> nameables, String type) {
        super(nameables, type);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return DescriptionArgumentType.super.listSuggestions(context, builder);
    }

}
