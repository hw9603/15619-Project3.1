import pandas as pd
tips = pd.read_table('yelp_academic_dataset_tip.tsv').user_id
reviews = pd.read_table('yelp_academic_dataset_review.tsv').user_id
print(pd.merge(tips.to_frame(), reviews.to_frame(), on='user_id')
        .user_id.drop_duplicates().count())
