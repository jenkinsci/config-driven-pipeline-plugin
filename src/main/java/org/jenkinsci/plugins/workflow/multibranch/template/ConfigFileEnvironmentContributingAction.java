package org.jenkinsci.plugins.workflow.multibranch.template;

import hudson.EnvVars;
import hudson.Extension;
import hudson.model.*;

import javax.annotation.Nonnull;

/**
 * Add the specified <code>configContents</code> to an environment variable called PIPELINE_CONFIG for a run with this
 * <code>Action</code>
 */
@Extension
public class ConfigFileEnvironmentContributingAction extends InvisibleAction implements EnvironmentContributingAction {

    public static final String PIPELINE_CONFIG = "PIPELINE_CONFIG";
    private String configContents;

    public ConfigFileEnvironmentContributingAction() {
        // @Extension annotated classes must have a public no-argument constructor
        super();
    }

    /**
     * Init with the <code>configContents</code> which should be added to PIPELINE_CONFIG
     * @param configContents
     */
    public ConfigFileEnvironmentContributingAction(String configContents) {
        this.configContents = configContents;
    }

    @Override
    public void buildEnvironment(@Nonnull Run<?, ?> run, @Nonnull EnvVars env) {
        env.put(PIPELINE_CONFIG, configContents);
    }
}
