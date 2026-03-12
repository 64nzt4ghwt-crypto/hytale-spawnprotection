package com.howlstudio.spawnprotection;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** SpawnProtection — Protect the spawn area. New players are safe from PvP. Configurable radius, bypass for staff. */
public final class SpawnProtectionPlugin extends JavaPlugin {
    private SpawnProtectionManager mgr;
    public SpawnProtectionPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[SpawnProtect] Loading...");
        mgr=new SpawnProtectionManager(getDataDirectory());
        CommandManager.get().register(mgr.getSpawnProtectCommand());
        System.out.println("[SpawnProtect] Ready. Radius="+mgr.getRadius());
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[SpawnProtect] Stopped.");}
}
