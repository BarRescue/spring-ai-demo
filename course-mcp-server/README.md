# Course MCP Server (demo)

Spring Boot 4.1 + Spring AI 2.0 MCP server that exposes hardcoded course data as tools.
Transport: **Streamable HTTP** at `POST /mcp`.

## Tools

| Tool | What it does | Example question |
|---|---|---|
| `search_courses` | Filters by category, level (BEGINNER/INTERMEDIATE/ADVANCED) and/or keyword | "Which AI courses are there for beginners?" |
| `get_course_details` | All details of a single course by code | "How much is SPRING-201 and where is it held?" |
| `list_course_categories` | List of categories | "Which topics do you offer training in?" |

The data lives in `CourseRepository` — change it to suit your demo.

## Running

```bash
mvn spring-boot:run
# or
mvn package && java -jar target/course-mcp-server-0.0.1-SNAPSHOT.jar
```

Requires Java 21+.

## Connecting to Claude

### Option A — Claude Code (local, fastest)

```bash
claude mcp add --transport http courses http://localhost:8080/mcp
claude mcp list   # should show ✔ Connected
```

### Option B — Claude (web/desktop) as a custom connector

Claude connects from Anthropic's cloud, so `localhost` won't work: the server must be
reachable from the public internet. For a demo, a tunnel is the easiest option:

```bash
cloudflared tunnel --url http://localhost:8080
# or: ngrok http 8080
```

Then in Claude: **Customize → Connectors → + Add → Add custom connector**
- URL: `https://<your-tunnel>/mcp`
- Authentication: **No sign in**

## Testing manually with curl

```bash
H='-H Content-Type:application/json -H Accept:application/json,text/event-stream'

# 1. initialize (take the Mcp-Session-Id from the response headers)
curl -si $H localhost:8080/mcp -d '{"jsonrpc":"2.0","id":1,"method":"initialize","params":{"protocolVersion":"2025-06-18","capabilities":{},"clientInfo":{"name":"curl","version":"1"}}}'

# 2. call the tools
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","method":"notifications/initialized"}'
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","id":2,"method":"tools/list"}'
curl -s $H -H "Mcp-Session-Id: <id>" localhost:8080/mcp -d '{"jsonrpc":"2.0","id":3,"method":"tools/call","params":{"name":"search_courses","arguments":{"category":"AI"}}}'
```

Or visually: `npx @modelcontextprotocol/inspector` → Streamable HTTP → `http://localhost:8080/mcp`.

## Note

There is no authentication — fine for a demo with a temporary tunnel, not for production.
