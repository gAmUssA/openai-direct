# 🛫\033[1;34m OpenAI Direct API Call Example\033[0m

all: build run

build:
	@echo "\033[1;32m🔨 Building...\033[0m"
	./gradlew build

run:
	@echo "\033[1;33m🚀 Running...\033[0m"
	OPENAI_API_KEY=$${OPENAI_API_KEY} ./gradlew run

httpie:
	@echo "\033[1;34m🌐 Running with HTTPie...\033[0m"
	http POST https://api.openai.com/v1/chat/completions \
	  "Authorization: Bearer $${OPENAI_API_KEY}" \
	  "Content-Type: application/json" \
	  model="gpt-3.5-turbo" \
	  messages:='[{"role": "user", "content": "What are the best ways to maximize airline loyalty program benefits?"}]'

httpie-content:
	@echo "\033[1;35m📝 Extracting content with jq...\033[0m"
	http POST https://api.openai.com/v1/chat/completions \
	  "Authorization: Bearer $${OPENAI_API_KEY}" \
	  "Content-Type: application/json" \
	  model="gpt-3.5-turbo" \
	  messages:='[{"role": "user", "content": "What are the best ways to maximize airline loyalty program benefits?"}]' | \
	  jq '.choices[0].message.content'

httpie-pretty:
	@echo "\033[1;36m🎨 Pretty-printing with jq...\033[0m"
	http POST https://api.openai.com/v1/chat/completions \
	  "Authorization: Bearer $${OPENAI_API_KEY}" \
	  "Content-Type: application/json" \
	  model="gpt-3.5-turbo" \
	  messages:='[{"role": "user", "content": "What are the best ways to maximize airline loyalty program benefits?"}]' | \
	  jq '.'

.PHONY: all build run httpie httpie-content httpie-pretty
