package io.musician101.musicianlibrary.java.minecraft.command;

import io.musician101.musicianlibrary.java.minecraft.MLResettableBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;

public abstract class AbstractCommand<A extends AbstractCommandArgument<M>, C extends AbstractCommand<A, C, I, M, P, S, U>, I, M, P extends AbstractCommandPermissions<M, S>, S, U extends AbstractCommandUsage<A, M>> {

    private BiFunction<S, List<String>, MLCommandResult> biFunction;
    private M description;
    private String name;
    private P permissions;
    private Map<String, C> subCommands;
    private U usage;

    private MLCommandResult execute(@Nonnull String arg, @Nonnull S sender, @Nonnull List<String> args) {
        if (subCommands.containsKey(arg)) {
            return subCommands.get(arg).execute(sender, shiftArguments(args));
        }

        return null;
    }

    @Nonnull
    public MLCommandResult execute(@Nonnull S sender, @Nonnull List<String> args) {
        if (!minArgsMet(args.size()))
            return MLCommandResult.NOT_ENOUGH_ARGUMENTS;

        MLCommandResult result = null;
        if (args.size() > 0)
            result = execute(args.get(0), sender, shiftArguments(args));

        return result == null ? biFunction.apply(sender, args) : result;
    }

    public M getDescription() {
        return description;
    }

    protected void setDescription(@Nonnull M description) {
        this.description = description;
    }

    @Nonnull
    public abstract M getHelp();

    public String getName() {
        return name;
    }

    protected void setName(@Nonnull String name) {
        this.name = name;
    }

    public Map<String, C> getSubCommands() {
        return subCommands;
    }

    protected void setSubCommands(@Nonnull Map<String, C> subCommands) {
        this.subCommands = subCommands;
    }

    @Nonnull
    public M getUsage() {
        return usage.getUsage();
    }

    protected void setUsage(@Nonnull U usage) {
        this.usage = usage;
    }

    protected boolean minArgsMet(int amount) {
        return usage.getMinArgs() < amount;
    }

    protected void setBiFunction(@Nonnull BiFunction<S, List<String>, MLCommandResult> biFunction) {
        this.biFunction = biFunction;
    }

    protected void setPermissions(@Nonnull P permissions) {
        this.permissions = permissions;
    }

    protected List<String> shiftArguments(@Nonnull String... args) {
        return shiftArguments(Arrays.asList(args));
    }

    protected List<String> shiftArguments(@Nonnull List<String> args) {
        if (!args.isEmpty())
            args.remove(0);

        return args;
    }

    protected boolean testPermissions(@Nonnull S sender) {
        return permissions.testPermissions(sender);
    }

    @Nonnull
    protected abstract C getHelpCommand(I plugin);

    protected static abstract class AbstractCommandBuilder<A extends AbstractCommandArgument<M>, B extends AbstractCommandBuilder<A, B, C, I, M, P, S, U>, C extends AbstractCommand<A, C, I, M, P, S, U>, I, M, P extends AbstractCommandPermissions<M, S>, S, U extends AbstractCommandUsage<A, M>> implements MLResettableBuilder<C, B> {

        protected BiFunction<S, List<String>, MLCommandResult> biFunction = (sender, args) -> MLCommandResult.SUCCESS;
        protected M description;
        protected String name;
        protected P permissions;
        protected Map<String, C> subCommands = new HashMap<>();
        protected U usage;

        @Nonnull
        public abstract B addCommand(@Nonnull C command);

        @Nonnull
        public abstract B function(@Nonnull BiFunction<S, List<String>, MLCommandResult> biFunction);

        @Nonnull
        public abstract B description(@Nonnull M description);

        @Nonnull
        public abstract B name(@Nonnull String name);

        @Nonnull
        public abstract B permissions(@Nonnull P permissions);

        @Nonnull
        public abstract B usage(@Nonnull U usage);
    }
}
