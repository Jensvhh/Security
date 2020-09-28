/*
 * Copyright Missionary (c) 2017. Unauthorized use of this work can and will result in further action against the unauthorized user(s).
 */
package me.siaco.security.utils;

/**
 * Created by Siaco
 */
import lombok.EqualsAndHashCode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
@EqualsAndHashCode(callSuper = true)
public class Config extends YamlConfiguration {
    private final String fileName;
    private final JavaPlugin plugin;

    public Config(JavaPlugin plugin, String fileName) {
        this(plugin, fileName, ".yml");
    }

    public Config(JavaPlugin plugin, String fileName, String fileExtension) {
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        this.createFile();
    }

    public String getFileName() {
        return this.fileName;
    }

    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    private void createFile() {
        File folder = this.plugin.getDataFolder();

        try {
            File file = new File(folder, this.fileName);
            if (!file.exists()) {
                if (this.plugin.getResource(this.fileName) != null) {
                    this.plugin.saveResource(this.fileName, false);
                } else {
                    this.save(file);
                }
            } else {
                this.load(file);
                this.save(file);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    public void save() {
        File folder = this.plugin.getDataFolder();

        try {
            this.save(new File(folder, this.fileName));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}