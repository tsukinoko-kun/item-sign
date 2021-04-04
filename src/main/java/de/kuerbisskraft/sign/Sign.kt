package de.kuerbisskraft.sign

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Sign : JavaPlugin(), CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (!sender.hasPermission("isign.use")) {
            sender.sendMessage("${ChatColor.RED}Du hast nicht die benötigte Berechtigung!")
            return false
        }

        val player = if (sender is Player) {
            sender
        } else {
            null
        }

        if (command.name != "isign" || player == null) {
            return false
        }

        val item = if (player.inventory.itemInMainHand.type.isItem) {
            player.inventory.itemInMainHand
        } else {
            player.inventory.itemInOffHand
        }

        if (!item.type.isItem) {
            player.sendMessage("${ChatColor.RED}Du musst ein Item in der Hand halten, um es zu signieren")
            return false
        }

        if (item.itemMeta == null) {
            player.sendMessage("${ChatColor.RED}Das Item ist leider nicht signierbar")
            return false
        }
        val meta = item.itemMeta!!

        val txt = args.joinToString(" ").replace("\\n", "\n").replace("/n", "\n")

        val current = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        val lore = "Signiert von ${ChatColor.GREEN}${player.name}${ChatColor.RESET} am ${ChatColor.GREEN}${current.format(dateFormatter)}${ChatColor.RESET} $txt"
        if (meta.lore != null) {
            meta.lore!!.add("----------------")
            meta.lore!!.add(lore)
        }
        else {
            meta.lore = listOf(lore)
        }

        item.itemMeta = meta

        sender.sendMessage("Item erfolgreich signiert!")

        return true
    }
}
