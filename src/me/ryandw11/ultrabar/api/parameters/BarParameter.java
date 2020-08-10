package me.ryandw11.ultrabar.api.parameters;

import me.ryandw11.ultrabar.api.BossBarBuilder;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Implement this interface to create custom bar parameters.
 * See {@link me.ryandw11.ultrabar.api.UltraBarAPI#registerParameter(BarParameter)} to register
 * your parameter.
 */
public interface BarParameter {
    /**
     * Your plugin name.
     *
     * @return Simply return your plugin name.
     */
    String getPluginName();

    /**
     * The aliases that your parameter can use.
     *
     * @return The list of aliases that your parameter can use.
     */
    List<String> aliases();

    /**
     * What should appear in the tab completion.
     *
     * @return What should appear in the tab completion.
     */
    String tab();

    /**
     * This is triggered when a bar with your parameter is created.
     *
     * @param value      The value that is provided with your parameter.
     * @param barBuilder The bar builder.
     * @param creator    The person who sent the command.
     */
    void barCreation(String value, BossBarBuilder barBuilder, CommandSender creator);

}
