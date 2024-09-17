package com.github.liuzhengyang.simpleapm.agent.command;

import com.github.liuzhengyang.simpleapm.agent.Terminal;
import com.github.liuzhengyang.simpleapm.agent.VertxServer;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.shell.cli.Completion;
import io.vertx.ext.shell.command.CommandBuilder;
import io.vertx.ext.shell.command.CommandProcess;
import io.vertx.ext.shell.command.CommandRegistry;

public interface ApmCommand {
    CommandBuilder getCommandBuilder();
    Handler<CommandProcess> getCommandProcessHandler();
    default Handler<Completion> getCompletionHandler() {
        return null;
    }
    default void registerCommand(Vertx vertx) {
        CommandBuilder commandBuilder = getCommandBuilder();
        commandBuilder.processHandler(process -> {
            Terminal.setCurrentProcess(process);
            getCommandProcessHandler().handle(process);
        });
        Handler<Completion> completionHandler = getCompletionHandler();
        if (completionHandler != null) {
            commandBuilder.completionHandler(completionHandler);
        }
        CommandRegistry registry = CommandRegistry.getShared(vertx);
        registry.registerCommand(commandBuilder.build(vertx));
    }
}
