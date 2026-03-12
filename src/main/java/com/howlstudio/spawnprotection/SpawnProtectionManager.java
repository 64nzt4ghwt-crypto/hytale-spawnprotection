package com.howlstudio.spawnprotection;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class SpawnProtectionManager {
    private final Path dataDir;
    private int radius=20;
    private boolean pvpBlocked=true;
    private boolean buildBlocked=true;
    private final Set<UUID> bypassList=new HashSet<>();
    public SpawnProtectionManager(Path d){this.dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}load();}
    public int getRadius(){return radius;}
    public boolean isPvPBlocked(){return pvpBlocked;}
    public boolean isBuildBlocked(){return buildBlocked;}
    public boolean hasBypass(UUID uid){return bypassList.contains(uid);}
    public void setRadius(int r){radius=r;save();}
    public void addBypass(UUID uid){bypassList.add(uid);}
    public void removeBypass(UUID uid){bypassList.remove(uid);}
    public void save(){try{String cfg="radius="+radius+"\npvp="+pvpBlocked+"\nbuild="+buildBlocked;Files.writeString(dataDir.resolve("config.txt"),cfg);}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("config.txt");if(!Files.exists(f))return;for(String l:Files.readAllLines(f)){if(l.startsWith("radius="))radius=Integer.parseInt(l.split("=")[1]);else if(l.startsWith("pvp="))pvpBlocked=Boolean.parseBoolean(l.split("=")[1]);else if(l.startsWith("build="))buildBlocked=Boolean.parseBoolean(l.split("=")[1]);}}catch(Exception e){}}
    public AbstractPlayerCommand getSpawnProtectCommand(){
        return new AbstractPlayerCommand("spawnprotect","[Admin] Manage spawn protection. /spawnprotect status|radius <n>|pvp|build|bypass <player>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String[]args=ctx.getInputString().trim().split("\\s+",2);
                String sub=args.length>0?args[0].toLowerCase():"status";
                switch(sub){
                    case"status"->{playerRef.sendMessage(Message.raw("[SpawnProtect] Radius: "+radius+" blocks | PvP blocked: "+pvpBlocked+" | Build blocked: "+buildBlocked+" | Bypass: "+bypassList.size()+" players"));}
                    case"radius"->{if(args.length<2)break;try{setRadius(Integer.parseInt(args[1]));playerRef.sendMessage(Message.raw("[SpawnProtect] Radius set to "+radius));}catch(Exception e){playerRef.sendMessage(Message.raw("Usage: /spawnprotect radius <number>"));}}
                    case"pvp"->{pvpBlocked=!pvpBlocked;save();playerRef.sendMessage(Message.raw("[SpawnProtect] PvP blocked: "+pvpBlocked));}
                    case"build"->{buildBlocked=!buildBlocked;save();playerRef.sendMessage(Message.raw("[SpawnProtect] Build blocked: "+buildBlocked));}
                    default->playerRef.sendMessage(Message.raw("Usage: /spawnprotect status|radius <n>|pvp|build"));
                }
            }
        };
    }
}
