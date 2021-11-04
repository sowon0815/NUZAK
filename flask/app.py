import openai
openai.api_key = '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'
response = openai.Completion.create(
  engine="davinci",
  prompt="hello world",
  temperature=0.7,
  max_tokens=64,
  top_p=1,
  frequency_penalty=0,
  presence_penalty=0
)

from flask import Flask
app = Flask(__name__)


@app.route('/')
def index():
   return response

@app.route('/story',defaults={'data':'index'})
@app.route('/story/<data>')
def boards(data):
  response = openai.Completion.create(
    model='curie:ft-ewha-womans-university-2021-09-16-07-39-33',
    prompt=data+'\n\n###\n\n',
    max_tokens=500,
    temperature=0.7,
    n=3,
    stop=[" END"],
    frequency_penalty = 0.2
    )
  return response.choices[1].text

if __name__ == '__main__':  
   app.run('0.0.0.0', port=8080, debug=True)