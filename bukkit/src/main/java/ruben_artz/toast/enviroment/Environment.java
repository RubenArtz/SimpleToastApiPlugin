package ruben_artz.toast.enviroment;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Environment {

    private final int minecraftVersion;
    private final int minecraftPatchVersion;
    private final int minecraftPreReleaseVersion;
    private final int minecraftReleaseCandidateVersion;

    public Environment() {
        this(Bukkit.getVersion());
    }

    Environment(final String bukkitVersion) {
        Pattern versionPattern = Pattern.compile("(?i)\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?(?: (Pre-Release|Release Candidate) )?(\\d)?\\)");
        Matcher matcher = versionPattern.matcher(bukkitVersion);
        int version = 0;
        int patchVersion = 0;
        int preReleaseVersion = -1;
        int releaseCandidateVersion = -1;
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            try {
                version = Integer.parseInt(matchResult.group(2), 10);
            } catch (Exception ignored) {
            }
            if (matchResult.groupCount() >= 3) {
                try {
                    patchVersion = Integer.parseInt(matchResult.group(3), 10);
                } catch (Exception ignored) {
                }
            }
            if (matchResult.groupCount() >= 5) {
                try {
                    final int ver = Integer.parseInt(matcher.group(5));
                    if (matcher.group(4).toLowerCase(Locale.ENGLISH).contains("pre")) {
                        preReleaseVersion = ver;
                    } else {
                        releaseCandidateVersion = ver;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        this.minecraftVersion = version;
        this.minecraftPatchVersion = patchVersion;
        this.minecraftPreReleaseVersion = preReleaseVersion;
        this.minecraftReleaseCandidateVersion = releaseCandidateVersion;
    }

    public boolean isVersion(int minor) {
        return isVersion(minor, 0);
    }

    public boolean isVersion(int minor, int patch) {
        return minecraftVersion > minor || (minecraftVersion >= minor && minecraftPatchVersion >= patch);
    }

    public boolean isSpigot() {
        return false;
    }

    public boolean isPaper() {
        return false;
    }
}