<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Carbon\Carbon;
use App\Models\Deposits;

class Members extends Model
{
    // Assuming you have a 'deposits' table related by member_id
    public function deposits()
    {
        return $this->hasMany(Deposits::class, 'member_id', 'member_id');
    }
}
