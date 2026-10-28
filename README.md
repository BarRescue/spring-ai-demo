# Course MCP demo

| Project | Port | What |
|---|---|---|
| `course-mcp-server` | 8080 | MCP server with course tools (hardcoded data) at `/mcp` |
| `course-chat-client` | 8081 | Chat app with an OpenAI model that uses those tools over MCP |

Quick start (Java 21+):

```bash
# terminal 1
cd course-mcp-server && mvn spring-boot:run

# terminal 2
export OPENAI_API_KEY=sk-...
cd course-chat-client && mvn spring-boot:run
# → http://localhost:8081
```

Connecting to Claude: see `course-mcp-server/README.md`.
