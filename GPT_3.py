import openai

openai.api_key = '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'

response = openai.Completion.create(
    model='curie:ft-user-pntgawz2mghmamx9iteqf1pz-2021-08-06-06-23-12',
    prompt='The Bat and the Lion\n\n###\n\n',
    max_tokens=500,
    temperature=0.7,
    n=3,
    stop=[" END"],
    frequency_penalty = 0.2
    )

print(response)
