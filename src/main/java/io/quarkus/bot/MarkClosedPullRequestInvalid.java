package io.quarkus.bot;

import java.io.IOException;

import javax.inject.Inject;

import org.jboss.logging.Logger;
import org.kohsuke.github.GHEventPayload;
import org.kohsuke.github.GHPullRequest;

import io.quarkiverse.githubapp.event.PullRequest;
import io.quarkus.bot.config.QuarkusBotConfig;

class MarkClosedPullRequestInvalid {

    private static final Logger LOG = Logger.getLogger(MarkClosedPullRequestInvalid.class);

    private static final String TRIAGE_INVALID_LABEL = "triage/invalid";

    @Inject
    QuarkusBotConfig quarkusBotConfig;

    void markClosedPullRequestInvalid(@PullRequest.Closed GHEventPayload.PullRequest pullRequestPayload) throws IOException {
        GHPullRequest pullRequest = pullRequestPayload.getPullRequest();

        if (pullRequest.isMerged()) {
            return;
        }

        if (!quarkusBotConfig.dryRun) {
            pullRequest.addLabels(TRIAGE_INVALID_LABEL);
        } else {
            LOG.info("Pull request #" + pullRequest.getNumber() + " - Add label: triage/invalid");
        }
    }
}