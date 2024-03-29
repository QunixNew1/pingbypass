package me.earth.pingbypass.server.headlessmc;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.earth.headlessmc.api.HeadlessMc;
import me.earth.headlessmc.command.AbstractCommand;
import me.earth.pingbypass.PingBypassApi;
import me.earth.pingbypass.api.command.CommandManager;
import me.earth.pingbypass.api.command.DelegatingCommandSource;
import me.earth.pingbypass.server.PingBypassServer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

// TODO: proper API for adding stuff to HMC!
public class HmcPbCommand extends AbstractCommand {
    public HmcPbCommand(HeadlessMc ctx) {
        super(ctx, "pingbypass", "Execute PingBypass commands.");
    }

    @Override
    public void execute(String... args) {
        // this is kinda eh
        PingBypassApi.instances().filter(pb -> pb instanceof PingBypassServer).findFirst().ifPresent(server -> {
            if (args.length <= 1) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("Please specify a PingBypass command!").withStyle(ChatFormatting.RED));
                return;
            }
            // takeout the "pingbypass" at the start of the args
            String[] pbArgs = new String[args.length - 1];
            System.arraycopy(args, 1, pbArgs, 0, args.length - 1);
            CommandManager cm = server.getCommandManager();
            try {
                cm.execute(String.join(" ", pbArgs), new DelegatingCommandSource(Minecraft.getInstance(), server));
            } catch (CommandSyntaxException e) {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal(e.getMessage()).withStyle(ChatFormatting.RED));
            }
        });
    }

}
